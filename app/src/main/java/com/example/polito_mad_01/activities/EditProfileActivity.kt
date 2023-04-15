package com.example.polito_mad_01.activities

import com.example.polito_mad_01.viewmodel.EditProfileViewModel
import android.Manifest
import android.app.*
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.polito_mad_01.R

class EditProfileActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654
    private val PERMISSION_REQUEST_CODE = 200
    private val vm by viewModels<EditProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> R.layout.activity_edit_profile_landscape
            else -> R.layout.activity_edit_profile_portrait
        }
        setContentView(layout)

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

        getData()
        setListeners()
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
        vm.imageUriString.value = imageUri.toString()
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
            vm.imageUriString.value = imageUri.toString()
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
            val i = Intent(this, ShowProfileActivity::class.java)
            startActivity(i)
            true
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun isNotValid() {
        fieldIsValid(vm.fullName.value, "Full Name")
        fieldIsValid(vm.nickname.value, "Nickname")
        fieldIsValid(vm.description.value, "Description")
        fieldIsValid(vm.email.value, "Email")
        fieldIsValid(vm.phoneNumber.value, "Phone Number")
        fieldIsValid(vm.location.value, "Location")
        fieldIsValid(vm.age.value, "Age")

        val regexAge = Regex("^\\d{1,3}\$")
        if (!regexAge.matches(vm.age.value!!)) {
            throw Exception("Age should be a number of max 3 digits")
        }

        val regexMail = Regex("^[A-Za-z\\d+_.-]+@(.+)\$")
        if (!regexMail.matches(vm.email.value!!)) {
            throw Exception("invalid email format")
        }

        val regexPhone = Regex("^\\d{10}\$")
        if (!regexPhone.matches(vm.phoneNumber.value!!)) {
            throw Exception("Phone number should be a 10 digit number")
        }

        val expertList = vm.expertList.value
        val intermediateList = vm.intermediateList.value
        val beginnerList = vm.beginnerList.value
        if (expertList.isNullOrEmpty() && intermediateList.isNullOrEmpty() && beginnerList.isNullOrEmpty()) {
            throw Exception("at least one skill")
        }

        imageUri?.let {
            vm.imageUriString.value = it.toString()
        }
    }

    private fun fieldIsValid(field: String?, fieldName: String) {
        if (field.isNullOrEmpty())
            throw Exception("$fieldName is invalid")
    }

    private fun getData() {
        if (!vm.changing) {
            //get user from database
        } else {
            vm.changing = false
        }
        setAllView()
    }

    private fun setAllView() {

        setEditTextView(R.id.fullName_value,vm.fullName)
        setEditTextView(R.id.nickName_value,vm.nickname)
        setEditTextView(R.id.description_value,vm.description)
        setEditTextView(R.id.age_value,vm.age)
        setEditTextView(R.id.mail_value,vm.email)
        setEditTextView(R.id.location_value,vm.location)
        setEditTextView(R.id.phoneNumber_value,vm.phoneNumber)
        setEditTextView(R.id.monHours_value,vm.mondayAvailability)
        setEditTextView(R.id.tueHours_value,vm.tuesdayAvailability)
        setEditTextView(R.id.wedHours_value,vm.wednesdayAvailability)
        setEditTextView(R.id.thuHours_value,vm.thursdayAvailability)
        setEditTextView(R.id.friHours_value,vm.fridayAvailability)
        setEditTextView(R.id.satHours_value,vm.saturdayAvailability)
        setEditTextView(R.id.sunHours_value,vm.sundayAvailability)
        findViewById<Spinner>(R.id.spinner).setSelection(vm.genderIndex.value ?: 0)

        try {
            vm.imageUriString.value?.let {
                println(" IMAGE STRING $it")
                println(" IMAGE URI ${it.toUri()}")
                if (it.isNotEmpty()) {
                    imageUri = it.toUri()
                    findViewById<ImageView>(R.id.profileImage_imageView).setImageURI(imageUri)
                }
            }
        } catch (e: Exception) {
            println("EXCEPTION ${e.message}")
        }

    }

    private fun setListeners() {
        setOneListener(R.id.fullName_value, vm.fullName)
        setOneListener(R.id.nickName_value, vm.nickname)
        setOneListener(R.id.description_value, vm.description)
        setOneListener(R.id.age_value, vm.age)
        setOneListener(R.id.mail_value, vm.email)
        setOneListener(R.id.phoneNumber_value, vm.phoneNumber)
        setOneListener(R.id.location_value, vm.location)
        setOneListener(R.id.monHours_value, vm.mondayAvailability)
        setOneListener(R.id.tueHours_value, vm.tuesdayAvailability)
        setOneListener(R.id.wedHours_value, vm.wednesdayAvailability)
        setOneListener(R.id.thuHours_value, vm.thursdayAvailability)
        setOneListener(R.id.friHours_value, vm.fridayAvailability)
        setOneListener(R.id.satHours_value, vm.saturdayAvailability)
        setOneListener(R.id.sunHours_value, vm.sundayAvailability)

        findViewById<Spinner>(R.id.spinner).onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long) {
                    vm.genderIndex.value = position
                    val arrayID = R.array.genderArray
                    val array = resources.getStringArray(arrayID)
                    vm.gender.value = array[position]
                }
            }
    }

    private fun setOneListener(id: Int, field: MutableLiveData<String>) {
        findViewById<EditText>(id).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                field.value = s.toString()
            }
        })
    }

    private fun setEditTextView(id : Int, field : MutableLiveData<String>){
        findViewById<EditText>(id).setText(field.value ?: "")
    }

}

