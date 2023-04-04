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

class DataViewModel: ViewModel() {
    private var _formData = MutableLiveData<JSONObject>().apply { value = JSONObject() }
    var changing: Boolean = false

    val formData: LiveData<JSONObject>
        get() = _formData

    fun addField(key: String, value: Any) : DataViewModel { _formData.value?.put(key, value); return this }

    fun clearFields() { _formData = MutableLiveData<JSONObject>().apply { value = JSONObject() } }
}

class EditProfileActivity : AppCompatActivity() {

    private lateinit var frame: ImageView
    private lateinit var spinner: Spinner
    private  var imageUri: Uri? = null
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
        ArrayAdapter.createFromResource(this, R.array.genderArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        frame = findViewById(R.id.profileImage_imageView)

        // calling the action bar
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Edit Profile"
        }

        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            val imgButton = findViewById<ImageButton>(R.id.imageButton)
            registerForContextMenu(imgButton)

            imgButton.setOnClickListener { v -> v.showContextMenu() }
        }

        if(vm.changing){
            getViewModelData()
            vm.changing = false
            vm.clearFields()
        } else {
            getData()
        }

    }

    fun getViewModelData(){
        val userObject = vm.formData.value!!

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
        setTextView("mail", R.id.mail_value, userObject)
        setTextView("phoneNumber", R.id.phoneNumber_value, userObject)
        spinner.setSelection(userObject.getInt("genderIndex"))

        userObject.getString("image_data")?.let {
            imageUri = it.toUri()
            findViewById<ImageView>(R.id.profileImage_imageView).setImageURI(imageUri)
        }
    }

    override fun onPause() {
        super.onPause()

        if(isChangingConfigurations){
            vm.changing = true

            vm.addField("fullName", getEditText(R.id.fullName_value))
                .addField("nickname", getEditText(R.id.nickName_value))
                .addField("genderIndex", spinner.selectedItemPosition)
                .addField("age", getEditText(R.id.age_value))
                .addField("location", getEditText(R.id.location_value))
                .addField("description", getEditText(R.id.description_value))
                .addField("expertList", getEditText(R.id.expertList_value))
                .addField("intermediateList", getEditText(R.id.intermediateList_value))
                .addField("beginnerList", getEditText(R.id.beginnerList_value))
                .addField("monday", getEditText(R.id.monHours_value))
                .addField("tuesday", getEditText(R.id.tueHours_value))
                .addField("wednesday",  getEditText(R.id.wedHours_value))
                .addField("thursday", getEditText(R.id.thuHours_value))
                .addField("friday", getEditText(R.id.friHours_value))
                .addField("saturday", getEditText(R.id.satHours_value))
                .addField("sunday", getEditText(R.id.sunHours_value))
                .addField("mail", getEditText(R.id.mail_value))
                .addField("phoneNumber", getEditText(R.id.phoneNumber_value))
                .addField("image_data", imageUri?: "" )
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?,
        v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
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
            when(frame.drawable){
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

            when(frame.drawable){
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
            != PackageManager.PERMISSION_GRANTED) return false
        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun Activity.showPermissionReasonAndRequest(title: String,message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> requestPermission()}
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

        val user = JSONObject()
        var text = getEditText(R.id.fullName_value)
        if (!toastForEmptyFields(text, "Please enter your full name"))
            return false
        user.put("fullName", text)

        text = getEditText(R.id.nickName_value)
        if (!toastForEmptyFields(text, "Please enter your nickname"))
            return false
        user.put("nickname", text)

        text = getEditText(R.id.age_value)
        if (!toastForEmptyFields(text, "Please enter your age"))
            return false
        user.put("age", text)

        user.put("genderIndex", spinner.selectedItemPosition)

        text = getEditText(R.id.location_value)
        if (!toastForEmptyFields(text, "Please enter your location"))
            return false
        user.put("location", text)

        text = getEditText(R.id.description_value)
        if (!toastForEmptyFields(text, "Please enter your description"))
            return false
        user.put("description", text)

        val expertList = getEditText(R.id.expertList_value)
        val intermediateList = getEditText(R.id.intermediateList_value)
        val beginnerList = getEditText(R.id.beginnerList_value)
        if(expertList.isEmpty() && intermediateList.isEmpty() && beginnerList.isEmpty()){
            Toast.makeText(this, "Please enter at least one skill", Toast.LENGTH_SHORT).show()
            return false
        }

        text = getEditText(R.id.mail_value)
        if (!toastForEmptyFields(text, "Please enter your mail"))
            return false
        user.put("email", text)

        text = getEditText(R.id.phoneNumber_value)
        if (!toastForEmptyFields(text, "Please enter your phone number"))
            return false
        user.put("phoneNumber", text)

        user.put("expertList", expertList)
            .put("intermediateList", intermediateList)
            .put("beginnerList", beginnerList)
            .put("monday", getEditText(R.id.monHours_value))
            .put("tuesday", getEditText(R.id.tueHours_value))
            .put("wednesday",  getEditText(R.id.wedHours_value))
            .put("thursday", getEditText(R.id.thuHours_value))
            .put("friday", getEditText(R.id.friHours_value))
            .put("saturday", getEditText(R.id.satHours_value))
            .put("sunday", getEditText(R.id.sunHours_value))
            .put("image_data", imageUri?: "" )

        getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE).edit()
            .putString("user", "$user")
            .apply()
        return true
    }

    private fun getData() {
        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)

        //extract a json object from a string
        val userString = sp.getString("user", null)
        userString?.let {
            val userObject = JSONObject(userString)

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

    private fun toastForEmptyFields(textToBeChecked: String, textToBeDisplayed: String): Boolean {
        if (textToBeChecked.isEmpty()) {
            Toast.makeText(this, textToBeDisplayed, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun setTextView( key: String, id: Int, json: JSONObject){
        findViewById<TextView>(id).text = json.getString(key)?: ""
    }

    private fun getEditText( id: Int): String{
        return findViewById<EditText>(id).text.toString()
    }

}

