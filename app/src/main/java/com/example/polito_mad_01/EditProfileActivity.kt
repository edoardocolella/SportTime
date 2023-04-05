package com.example.polito_mad_01

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject

class DataViewModel : ViewModel() {
    private var _formData = MutableLiveData<JSONObject>().apply { value = JSONObject() }
    var changing: Boolean = false
    val formData: LiveData<JSONObject>
        get() = _formData

    fun addField(key: String, value: Any): DataViewModel {
        _formData.value?.put(key, value); return this
    }

    fun setObject(obj: JSONObject) {
        _formData.value = obj
    }

    fun clearFields() {
        _formData = MutableLiveData<JSONObject>().apply { value = JSONObject() }
    }
}

class EditProfileActivity : AppCompatActivity() {

    private lateinit var frame: ImageView
    private lateinit var spinner: Spinner
    private var imageUri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654
    private val PERMISSION_REQUEST_CODE = 200

    private val vm by viewModels<DataViewModel>()

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
        setListener(R.id.fullName_value, "fullName")
        setListener(R.id.nickName_value, "nickname")
        setListener(R.id.age_value, "age")
        setListener(R.id.location_value, "location")
        setListener(R.id.description_value, "description")
        setListener(R.id.expertList_value, "expertList")
        setListener(R.id.intermediateList_value, "intermediateList")
        setListener(R.id.beginnerList_value, "beginnerList")
        setListener(R.id.monHours_value, "monday")
        setListener(R.id.tueHours_value, "tuesday")
        setListener(R.id.wedHours_value, "wednesday")
        setListener(R.id.thuHours_value, "thursday")
        setListener(R.id.friHours_value, "friday")
        setListener(R.id.satHours_value, "saturday")
        setListener(R.id.sunHours_value, "sunday")
        setListener(R.id.mail_value, "email")
        setListener(R.id.phoneNumber_value, "phoneNumber")

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                vm.addField("genderIndex", position)
            }
        }
    }

    private fun setListener(id: Int, field: String) {
        findViewById<EditText>(id).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.addField(field, s.toString())
            }
        })
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_picture, menu)
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            frame.setImageURI(imageUri)
            when (frame.drawable) {
                is BitmapDrawable -> {
                    val drawable = frame.drawable as BitmapDrawable
                    val bitmap = drawable.bitmap
                    val resized = bitmap?.let { Bitmap.createScaledBitmap(it, 400, 400, true) }
                    frame.setImageBitmap(resized)
                }
                else -> {
                    println("NO")
                }
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            frame.setImageURI(imageUri)

            when (frame.drawable) {
                is BitmapDrawable -> {
                    val drawable = frame.drawable as BitmapDrawable
                    val bitmap = drawable.bitmap
                    val resized = bitmap?.let { Bitmap.createScaledBitmap(it, 400, 400, true) }
                    frame.setImageBitmap(resized)
                }
                else -> {
                    println("NO")
                }
            }
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
                        "Hi, we will request CAMERA permission. This is required for taking the photo from camera, please grant it."
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
        if (!toastForEmptyFields(user.getString("fullName"), "your full name"))
            return false
        if (!toastForEmptyFields(user.getString("nickname"), "your nickname"))
            return false
        if (!toastForEmptyFields(user.getString("age"), "your age"))
            return false
        if (!toastForEmptyFields(user.getString("location"), "your location"))
            return false
        if (!toastForEmptyFields(user.getString("description"), "your description"))
            return false
        val expertList = user.getString("expertList")
        val intermediateList = user.getString("intermediateList")
        val beginnerList = user.getString("beginnerList")
        if (expertList.isEmpty() && intermediateList.isEmpty() && beginnerList.isEmpty()) {
            Toast.makeText(this, "at least one skill", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!toastForEmptyFields(user.getString("email"), "your mail"))
            return false
        if (!toastForEmptyFields(user.getString("phoneNumber"), " your phone number"))
            return false

        user.put("image_data", imageUri ?: "")

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

    private fun setTextView(key: String, id: Int, json: JSONObject) {
        findViewById<TextView>(id).text = json.getString(key) ?: ""
    }

    private fun setAllView(userObject: JSONObject) {
        setTextView("fullName", R.id.fullName_value, userObject)
        setTextView("nickname", R.id.nickName_value, userObject)
        setTextView("age", R.id.age_value, userObject)
        setTextView("description", R.id.description_value, userObject)
        setTextView("location", R.id.location_value, userObject)
        setTextView("expertList", R.id.expertList_value, userObject)
        setTextView("intermediateList", R.id.intermediateList_value, userObject)
        setTextView("beginnerList", R.id.beginnerList_value, userObject)
        setTextView("monday", R.id.monHours_value, userObject)
        setTextView("tuesday", R.id.tueHours_value, userObject)
        setTextView("wednesday", R.id.wedHours_value, userObject)
        setTextView("thursday", R.id.thuHours_value, userObject)
        setTextView("friday", R.id.friHours_value, userObject)
        setTextView("saturday", R.id.satHours_value, userObject)
        setTextView("sunday", R.id.sunHours_value, userObject)
        setTextView("email", R.id.mail_value, userObject)
        setTextView("phoneNumber", R.id.phoneNumber_value, userObject)
        spinner.setSelection(userObject.getInt("genderIndex"))

        userObject.getString("image_data")?.let {
            imageUri = it.toUri()
            findViewById<ImageView>(R.id.profileImage_imageView).setImageURI(imageUri)
        }
    }


}

