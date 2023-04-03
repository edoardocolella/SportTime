package com.example.polito_mad_01

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import org.json.JSONObject


class EditProfileActivity : AppCompatActivity() {

    private var frame: ImageView? = null
    private val PERMISSION_REQUEST_CODE = 200
    private var imageUri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654
    lateinit var spinner: Spinner

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

        val imgButton = findViewById<ImageButton>(R.id.imageButton)
        registerForContextMenu(imgButton)

        imgButton.setOnClickListener { v -> v.showContextMenu() }
        getData()

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
            frame?.setImageURI(imageUri)
            val drawable = frame?.drawable as BitmapDrawable
            val bitmap = drawable.bitmap
            val resized = bitmap?.let { Bitmap.createScaledBitmap(it, 400, 400, true) }
            frame?.setImageBitmap(resized)
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            frame?.setImageURI(imageUri)
            val drawable = frame?.drawable as BitmapDrawable
            val bitmap = drawable.bitmap
            val resized = bitmap?.let { Bitmap.createScaledBitmap(it, 400, 400, true) }
            frame?.setImageBitmap(resized)
            /*val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            encodedImage= Base64.encodeToString(b, Base64.DEFAULT)*/
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
                        "Hi, we will request CAMERA permission. " +
                                "This is required for taking the photo from camera, " +
                                "please grant it."
                    )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun Activity.showPermissionReasonAndRequest(
        title: String,
        message: String,
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK") { _, _ ->
            requestPermission()
        }

        builder.setNegativeButton("CANCEL") { _, _ ->
            Toast.makeText(
                applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT
            ).show()
        }

        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_edit_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Are you sure?")
                .setMessage("All changes will be lost")
                .setPositiveButton("YES") { _, _ ->
                    finish()
                }
                .setNegativeButton("NO") { _, _ ->
                    Toast.makeText(
                        applicationContext,
                        android.R.string.no, Toast.LENGTH_SHORT
                    ).show()
                }
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
            return false;
        user.put("fullName", text)

        text = getEditText(R.id.nickName_value)
        if (!toastForEmptyFields(text, "Please enter your nickname"))
            return false;
        user.put("nickname", text)

        text = getEditText(R.id.age_value)
        if (!toastForEmptyFields(text, "Please enter your age"))
            return false;
        user.put("age", text)

        user.put("genderIndex", spinner.selectedItemPosition)

        text = getEditText(R.id.location_value)
        if (!toastForEmptyFields(text, "Please enter your location"))
            return false;
        user.put("location", text)

        text = getEditText(R.id.description_value)
        if (!toastForEmptyFields(text, "Please enter your description"))
            return false;
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
            return false;
        user.put("email", text)

        text = getEditText(R.id.phoneNumber_value)
        if (!toastForEmptyFields(text, "Please enter your phone number"))
            return false;
        user.put("phoneNumber", text)

        user.put("expertList", expertList)
            .put("intermediateList", intermediateList)
            .put("beginnerList", beginnerList)
            .put("monday", findViewById<EditText>(R.id.monHours_value).text)
            .put("tuesday", findViewById<EditText>(R.id.tueHours_value).text)
            .put("wednesday", findViewById<EditText>(R.id.wedHours_value).text)
            .put("thursday", findViewById<EditText>(R.id.thuHours_value).text)
            .put("friday", findViewById<EditText>(R.id.friHours_value).text)
            .put("saturday", findViewById<EditText>(R.id.satHours_value).text)
            .put("sunday", findViewById<EditText>(R.id.sunHours_value).text)
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

            setTextView(userObject.getString("fullName"), R.id.fullName_value)
            setTextView(userObject.getString("nickname"), R.id.nickName_value)
            setTextView(userObject.getString("age"), R.id.age_value)
            setTextView(userObject.getString("description"), R.id.description_value)
            setTextView(userObject.getString("location"), R.id.location_value)
            setTextView(userObject.getString("expertList"), R.id.expertList_value)
            setTextView(userObject.getString("intermediateList"), R.id.intermediateList_value)
            setTextView(userObject.getString("beginnerList"), R.id.beginnerList_value)
            setTextView(userObject.getString("monday"), R.id.monHours_value)
            setTextView(userObject.getString("tuesday"), R.id.tueHours_value)
            setTextView(userObject.getString("wednesday"), R.id.wedHours_value)
            setTextView(userObject.getString("thursday"), R.id.thuHours_value)
            setTextView(userObject.getString("friday"), R.id.friHours_value)
            setTextView(userObject.getString("saturday"), R.id.satHours_value)
            setTextView(userObject.getString("sunday"), R.id.sunHours_value)
            setTextView(userObject.getString("email"), R.id.mail_value)
            setTextView(userObject.getString("phoneNumber"), R.id.phoneNumber_value)
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
            return false;
        }
        return true;
    }

    private fun setTextView( text: String?, id: Int){
        findViewById<TextView>(id).text = text?: ""
    }

    private fun getEditText( id: Int): String{
        return findViewById<EditText>(id).text.toString()
    }

}

