package com.example.polito_mad_01

import com.example.polito_mad_01.viewmodel.EditProfileViewModel
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

import org.json.JSONException
import org.json.JSONObject

class EditProfileActivity : AppCompatActivity() {

    private lateinit var frame: ImageView
    private lateinit var spinner: Spinner
    private var imageUri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654
    private val PERMISSION_REQUEST_CODE = 200

    private val arrayOfPairIDField: Array<Pair<Int, String>> =
        arrayOf(
            Pair(R.id.fullName_value, "fullName"),
            Pair(R.id.nickName_value, "nickname"),
            Pair(R.id.age_value, "age"),
            Pair(R.id.location_value, "location"),
            Pair(R.id.description_value, "description"),
            Pair(R.id.expertList_value, "expertList"),
            Pair(R.id.intermediateList_value, "intermediateList"),
            Pair(R.id.beginnerList_value, "beginnerList"),
            Pair(R.id.monHours_value, "monday"),
            Pair(R.id.tueHours_value, "tuesday"),
            Pair(R.id.wedHours_value, "wednesday"),
            Pair(R.id.thuHours_value, "thursday"),
            Pair(R.id.friHours_value, "friday"),
            Pair(R.id.satHours_value, "saturday"),
            Pair(R.id.sunHours_value, "sunday"),
            Pair(R.id.mail_value, "email"),
            Pair(R.id.phoneNumber_value, "phoneNumber")
        )

    private val vm by viewModels<EditProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> R.layout.activity_edit_profile_landscape
            else -> R.layout.activity_edit_profile_portrait
        }
        setContentView(layout)
        spinner = findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this, R.array.genderArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        frame = findViewById(R.id.profileImage_imageView)

        // calling the action bar
        supportActionBar?.let { it.title = "Edit Profile"; it.setDisplayHomeAsUpEnabled(true) }

        // showing the back button in action bar

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val imgButton = findViewById<ImageButton>(R.id.imageButton)
            registerForContextMenu(imgButton)

            imgButton.setOnClickListener { v -> v.showContextMenu() }
        }

        getData()
        setListeners()
    }

    private fun setListeners() {
        arrayOfPairIDField.forEach {
        findViewById<EditText>(it.first).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.addField(it.second, s.toString())
            }
        })
    }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                vm.addField("genderIndex", position)
            }
        }
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
        vm.addField("image_data", imageUri.toString())
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {


            frame.setImageURI(imageUri)
            /*when (frame.drawable) {
                is BitmapDrawable -> {
                    resizeImage()
                }
                else -> {
                    println("NO")
                }
            }*/
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            vm.addField("image_data", imageUri.toString())

            frame.setImageURI(imageUri)

            /*when (frame.drawable) {
                is BitmapDrawable -> {
                    resizeImage()
                }
                else -> {
                    println("NO")
                }
            }*/
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.gallery -> {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
                return true
            }
            R.id.picture -> {
                if (checkPermission()) {
                    openCamera()
                } else
                    showPermissionReasonAndRequest(
                        "Notice",
                        R.string.cameraPermission.toString()
                    )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> requestPermission() }
            .setNegativeButton("CANCEL") { _, _ -> }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_edit_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setMessage("All changes will be lost")
                    .setPositiveButton("YES") { _, _ -> finish() }
                    .setNegativeButton("NO") { _, _ -> }
                    .show()
                return true
            }
            R.id.action_save_profile -> {
                if (saveData()) {
                    val i = Intent(this, ShowProfileActivity::class.java)
                    startActivity(i)
                    return true
                }
                return false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveData(): Boolean {
        val user = vm.formData.value!!
        arrayOf(
            Pair("fullName", "your full name"),
            Pair("nickname", "your nickname"),
            Pair("age", "your age"),
            Pair("location", "your location"),
            Pair("description", "your description"),
            Pair("email", "your mail"),
            Pair("phoneNumber", "your phone number")
        )
            .forEach {
                if (!toastForEmptyFields(user.getString(it.first), it.second))
                    return false
            }

        val regexAge = Regex("^[0-9]{1,3}\$")
        val regexMail = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        val regexPhone = Regex("^[0-9]{10}\$")

        if (!regexAge.matches(user.getString("age"))) {
            Toast.makeText(this, "Age should be a number of max 3 digits", Toast.LENGTH_LONG).show()
            return false
        }
        if (!regexMail.matches(user.getString("email"))) {
            Toast.makeText(this, "invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!regexPhone.matches(user.getString("phoneNumber"))) {
            Toast.makeText(this, "Phone number should be a 10 digit number", Toast.LENGTH_LONG).show()
            return false
        }

        val expertList = user.getString("expertList")
        val intermediateList = user.getString("intermediateList")
        val beginnerList = user.getString("beginnerList")
        if (expertList.isEmpty() && intermediateList.isEmpty() && beginnerList.isEmpty()) {
            Toast.makeText(this, "at least one skill", Toast.LENGTH_SHORT).show()
            return false
        }

        imageUri?.let {
            user.put("image_data", it)
        }

        getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE).edit()
            .putString("user", "$user")
            .apply()
        return true
    }

    private fun getData() {
        var userObject = JSONObject()
        if (vm.changing) {
            userObject = vm.formData.value!!
            vm.changing = false
            vm.clearFields()
        } else {
            val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
            //extract a json object from a string
            val userString = sp.getString("user", null)
            userString?.let {
                userObject = JSONObject(userString)
            }
            arrayOfPairIDField.forEach {
                try {
                    userObject.getString(it.second)
                } catch (e: JSONException) {
                    userObject.put(it.second, "")
                }
            }

            vm.setObject(userObject)
            setAllView(userObject)
        }
    }

    private fun toastForEmptyFields(textToBeChecked: String, textToBeDisplayed: String): Boolean {
        if (textToBeChecked.isEmpty()) {
            Toast.makeText(this, "Please enter $textToBeDisplayed", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun setAllView(userObject: JSONObject) {

        arrayOfPairIDField.forEach {
            val view = findViewById<TextView>(it.first)
            val text = try {
                userObject.getString(it.second)
            } catch (e: JSONException) {
                ""
            }
            view.text = text
        }

        try{
            spinner.setSelection(userObject.getInt("genderIndex"))
        }
        catch (e: JSONException){
            spinner.setSelection(0)
        }

        try {
            val imgUriString = userObject.getString("image_data")
            println(" IMAGE STRING ${imgUriString}" )
            println(" IMAGE URI ${imgUriString.toUri()}")
            if (imgUriString.isNotEmpty()) {
                imageUri = imgUriString.toUri()
                frame.setImageURI(imageUri)
            }
        }catch (e: JSONException){
            println("NO IMAGE")
        }

    }



}

