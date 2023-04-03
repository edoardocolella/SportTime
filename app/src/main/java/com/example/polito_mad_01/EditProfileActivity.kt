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
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class EditProfileActivity : AppCompatActivity() {

    private var frame: ImageView? = null

    //var imgButton: ImageButton? = null
    private lateinit var cropIntent: Intent
    private val PERMISSION_REQUEST_CODE = 200
    private var imageUri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654
    var encodedImage: String = ""
    lateinit var spinner: Spinner


    @SuppressLint("MissingInflatedId")
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

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
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
        builder.setMessage(message)

        builder.setPositiveButton("OK") { _, _ ->
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
                builder.setMessage("All changes will be lost")

                builder.setPositiveButton("YES") { _, _ ->
                    finish()
                }

                builder.setNegativeButton("NO") { _, _ ->
                    Toast.makeText(
                        applicationContext,
                        android.R.string.no, Toast.LENGTH_SHORT
                    ).show()
                }

                builder.show()

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

        var text = findViewById<EditText>(R.id.fullName_value).text.toString()
        if (!toastForEmptyFields(text, "Please enter your full name"))
            return false;
        user.put("fullName", text)

        text = findViewById<EditText>(R.id.nickName_value).text.toString()
        if (!toastForEmptyFields(text, "Please enter your nickname"))
            return false;
        user.put("nickname", text)

        text = findViewById<EditText>(R.id.age_value).text.toString()
        if (!toastForEmptyFields(text, "Please enter your age"))
            return false;
        user.put("age", text)


        user.put("genderIndex", spinner.selectedItemPosition)

        text = findViewById<EditText>(R.id.location_value).text.toString()
        if (!toastForEmptyFields(text, "Please enter your location"))
            return false;
        user.put("location", text)

        text = findViewById<EditText>(R.id.description_value).text.toString()
        if (!toastForEmptyFields(text, "Please enter your description"))
            return false;
        user.put("description", text)

        val expertList = findViewById<EditText>(R.id.expertList_value).text.toString()
        val intermediateList = findViewById<EditText>(R.id.intermediateList_value).text.toString()
        val beginnerList = findViewById<EditText>(R.id.beginnerList_value).text.toString()

        if(expertList.isEmpty() && intermediateList.isEmpty() && beginnerList.isEmpty()){
            Toast.makeText(this, "Please enter at least one skill", Toast.LENGTH_SHORT).show()
            return false
        }

        text = findViewById<EditText>(R.id.mail_value).text.toString()
        if (!toastForEmptyFields(text, "Please enter your mail"))
            return false;
        user.put("email", text)

        text = findViewById<EditText>(R.id.phoneNumber_value).text.toString()
        if (!toastForEmptyFields(text, "Please enter your phone number"))
            return false;
        user.put("phoneNumber", text)

        user.put("expertList", expertList)
        user.put("intermediateList", intermediateList)
        user.put("beginnerList", beginnerList)
        user.put("monday", findViewById<EditText>(R.id.monHours_value).text)
        user.put("tuesday", findViewById<EditText>(R.id.tueHours_value).text)
        user.put("wednesday", findViewById<EditText>(R.id.wedHours_value).text)
        user.put("thursday", findViewById<EditText>(R.id.thuHours_value).text)
        user.put("friday", findViewById<EditText>(R.id.friHours_value).text)
        user.put("saturday", findViewById<EditText>(R.id.satHours_value).text)
        user.put("sunday", findViewById<EditText>(R.id.sunHours_value).text)

        user.put("image_data", imageUri?: "" )

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

            findViewById<TextView>(R.id.fullName_value).text =
                userObject.getString("fullName") ?: ""
            findViewById<TextView>(R.id.nickName_value).text =
                userObject.getString("nickname") ?: ""
            findViewById<TextView>(R.id.age_value).text = userObject.getString("age") ?: ""
            findViewById<TextView>(R.id.description_value).text =
                userObject.getString("description") ?: ""

            spinner.setSelection(userObject.getInt("genderIndex"))

            findViewById<TextView>(R.id.location_value).text =
                userObject.getString("location") ?: ""

            findViewById<TextView>(R.id.expertList_value).text =
                userObject.getString("expertList") ?: ""
            findViewById<TextView>(R.id.intermediateList_value).text =
                userObject.getString("intermediateList") ?: ""
            findViewById<TextView>(R.id.beginnerList_value).text =
                userObject.getString("beginnerList") ?: ""

            findViewById<TextView>(R.id.monHours_value).text = userObject.getString("monday") ?: ""
            findViewById<TextView>(R.id.tueHours_value).text = userObject.getString("tuesday") ?: ""
            findViewById<TextView>(R.id.wedHours_value).text =
                userObject.getString("wednesday") ?: ""
            findViewById<TextView>(R.id.thuHours_value).text =
                userObject.getString("thursday") ?: ""
            findViewById<TextView>(R.id.friHours_value).text = userObject.getString("friday") ?: ""
            findViewById<TextView>(R.id.satHours_value).text =
                userObject.getString("saturday") ?: ""
            findViewById<TextView>(R.id.sunHours_value).text = userObject.getString("sunday") ?: ""
            findViewById<TextView>(R.id.mail_value).text = userObject.getString("email") ?: ""
            findViewById<TextView>(R.id.phoneNumber_value).text =
                userObject.getString("phoneNumber") ?: ""

            var image :String? = userObject.getString("image_data")

            image?.let {
                findViewById<ImageView>(R.id.profileImage_imageView).setImageURI(it.toUri())
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
}

