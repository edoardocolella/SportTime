package com.example.polito_mad_01.activities_legacy

import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.net.ParseException
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.*
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.polito_mad_01.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.viewmodel.*
import java.util.*


class EditProfileActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654
    private val PERMISSION_REQUEST_CODE = 200

    private var imageUriString: String? = null

    private val vm: EditProfileViewModel by viewModels {
        EditProfileViewModelFactory((application as SportTimeApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.edit_profile)

        ArrayAdapter.createFromResource(
            this, R.array.genderArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            findViewById<Spinner>(R.id.spinner).adapter = adapter
        }

        supportActionBar?.let { it.title = "Edit Profile"; it.setDisplayHomeAsUpEnabled(true) }

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val imgButton = findViewById<ImageButton>(R.id.imageButton)
            registerForContextMenu(imgButton)
            imgButton.setOnClickListener { v -> v.showContextMenu() }
        }

        setAllView()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.menu_picture, menu)
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        imageUriString = imageUri.toString()
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        //startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val frame = findViewById<ImageView>(R.id.profileImage_imageView)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK)
            frame.setImageURI(imageUri)

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            imageUriString = imageUri.toString()
            frame.setImageURI(imageUri)
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.gallery -> gallery()
            R.id.picture -> picture()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun gallery(): Boolean {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
        return true
    }

    private fun picture(): Boolean {
        if (checkPermission()) {
            openCamera()
        } else
            showPermissionReasonAndRequest(
                "Notice",
                R.string.cameraPermission.toString()
            )
        return true
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) return false
        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun Activity.showPermissionReasonAndRequest(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title).setMessage(message)
            .setPositiveButton("OK") { _, _ -> requestPermission() }
            .setNegativeButton("CANCEL") { _, _ -> }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_edit_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> showExitDialog()
            R.id.action_save_profile -> trySaveData()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showExitDialog(): Boolean {
        AlertDialog.Builder(this)
            .setTitle("Are you sure?").setMessage("All changes will be lost")
            .setPositiveButton("YES") { _, _ -> finish() }
            .setNegativeButton("NO") { _, _ -> }.show()
        return true
    }

    private fun trySaveData(): Boolean {
        return try {
            isNotValid()
            vm.updateUser()
            val i = Intent(this, ShowProfileActivity::class.java)
            startActivity(i)
            true
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun isNotValid() {
        val user = vm.user.value!!

        fieldIsValid(user.name, "Full Name")
        fieldIsValid(user.nickname, "Nickname")
        fieldIsValid(user.description, "Description")
        fieldIsValid(user.email, "Email")
        fieldIsValid(user.phoneNumber, "Phone Number")
        fieldIsValid(user.location, "Location")
        fieldIsValid(user.birthdate, "BirthDate")

        val regexMail = Regex("^[A-Za-z\\d+_.-]+@(.+)\$")
        if (!regexMail.matches(user.email)) {
            throw Exception("invalid email format")
        }

        try {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatter.parse(user.birthdate)
        } catch (e: ParseException) {
            throw Exception("Birthdate should be in dd-MM-yyyy format")
        }

        val regexPhone = Regex("^\\d{10}\$")
        if (!regexPhone.matches(user.phoneNumber)) {
            throw Exception("Phone number should be a 10 digit number")
        }

        //check favouriteSport

        imageUri?.let { user.image_uri = it.toString() }

    }

    private fun fieldIsValid(field: String?, fieldName: String) {
        if (field.isNullOrEmpty())
            throw Exception("$fieldName is invalid")
    }

    private fun setAllView() {
        /*val userWithSkills = vm.getUserWithSkills(1)
        userWithSkills.observe(this) {
            vm.user.value = it
            if (it == null) return@observe
            val user = it.user
            val skills = it.skills

            setEditTextViewAndListener(R.id.fullName_value, user.name, myUser.name)
            //setEditTextViewAndListener(R.id.surname_value, user.surname, user::setSurname)
            setEditTextViewAndListener(R.id.nickName_value, user.nickname, user::setNickname)
            setEditTextViewAndListener(
                R.id.description_value,
                user.description,
                user::setDescription
            )
            setEditTextViewAndListener(R.id.age_value, user.birthdate, user::setBirthdate)
            setEditTextViewAndListener(R.id.location_value, user.location, user::setLocation)
            setEditTextViewAndListener(R.id.mail_value, user.email, user::setEmail)
            setEditTextViewAndListener(
                R.id.phoneNumber_value,
                user.phoneNumber,
                user::setPhoneNumber
            )
            setEditTextViewAndListener(
                R.id.monHours_value,
                user.mondayAvailability,
                user::setMondayAvailability
            )
            setEditTextViewAndListener(
                R.id.tueHours_value,
                user.tuesdayAvailability,
                user::setTuesdayAvailability
            )
            setEditTextViewAndListener(
                R.id.wedHours_value,
                user.wednesdayAvailability,
                user::setWednesdayAvailability
            )
            setEditTextViewAndListener(
                R.id.thuHours_value,
                user.thursdayAvailability,
                user::setThursdayAvailability
            )
            setEditTextViewAndListener(
                R.id.friHours_value,
                user.fridayAvailability,
                user::setFridayAvailability
            )
            setEditTextViewAndListener(
                R.id.satHours_value,
                user.saturdayAvailability,
                user::setSaturdayAvailability
            )
            setEditTextViewAndListener(
                R.id.sunHours_value,
                user.sundayAvailability,
                user::setSundayAvailability
            )

            //convert gender to index
            //findViewById<Spinner>(R.id.spinner).setSelection(vm.genderIndex.value ?: 0)

            user.image_uri?.let { uri ->
                if (uri.isNotEmpty()) {
                    imageUri = uri.toUri()
                    findViewById<ImageView>(R.id.profileImage_imageView).setImageURI(imageUri)
                }
            }

            skills.let { skillList ->
                val expertList = skillList
                    .filter { skill -> skill.skill_level == "Expert" }
                    .joinToString(" - ") { skill -> skill.sportName }
                val intermediateList = skillList
                    .filter { skill -> skill.skill_level == "Intermediate" }
                    .joinToString(" - ") { skill -> skill.sportName }
                val beginnerList = skillList
                    .filter { skill -> skill.skill_level == "Beginner" }
                    .joinToString(" - ") { skill -> skill.sportName }

                findViewById<TextView>(R.id.expertList).text = expertList
                findViewById<TextView>(R.id.intermediateList).text = intermediateList
                findViewById<TextView>(R.id.beginnerList).text = beginnerList
            }

            val spinner = findViewById<Spinner>(R.id.spinner)
            val arrayID = R.array.genderArray
            val array = resources.getStringArray(arrayID)
            spinner.setSelection(array.indexOf(user.gender))
            spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        user.setGender(array[position])
                    }
                }
        }*/

    }

    private fun setEditTextViewAndListener(viewId: Int, value: String?, setter: (String) -> Unit) {
        value?.let { setEditTextView(viewId, it) }
        setOneListener(viewId, setter)
    }

    private fun setEditTextView(id: Int, field: String?) {
        field?.let { findViewById<EditText>(id).setText(field) }
    }

    private fun setOneListener(id: Int, setter: (String) -> Unit) {
        findViewById<EditText>(id).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setter(s.toString())
            }
        })
    }
}

