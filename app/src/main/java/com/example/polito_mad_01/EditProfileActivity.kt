package com.example.polito_mad_01

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class EditProfileActivity : AppCompatActivity() {

    var frame: ImageView? = null
    //var imgButton: ImageButton? = null
    private lateinit var cropIntent:Intent
    var PERMISSION_REQUEST_CODE = 200
    var image_uri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    val IMAGE_CAPTURE_CODE = 654

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_portrait)
        frame = findViewById(R.id.profileImage_imageView)
        val imgButton = findViewById<ImageButton>(R.id.imageButton)

        getData()


        registerForContextMenu(imgButton)

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
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            frame?.setImageURI(image_uri)
            val drawable = frame?.drawable as BitmapDrawable
            val bitmap = drawable.bitmap
            val resized = bitmap?.let { Bitmap.createScaledBitmap(it, 400, 400, true) }
            frame?.setImageBitmap(resized)
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            image_uri = data.data!!
            frame?.setImageURI(image_uri)
            val drawable = frame?.drawable as BitmapDrawable
            val bitmap = drawable.bitmap
            val resized = bitmap?.let { Bitmap.createScaledBitmap(it, 400, 400, true) }
            frame?.setImageBitmap(resized)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item!!.itemId) {
            R.id.gallery -> {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)

                return true
            }
            R.id.picture -> {
                if(checkPermission()){
                    openCamera()
                }else
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

    fun checkPermission(): Boolean{
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


    fun requestPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE);
    }


    fun Activity.showPermissionReasonAndRequest(
        title: String,
        message: String,
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton("OK") { dialog, which ->
            requestPermission()
        }

        builder.setNegativeButton("CANCEL") { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_edit_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        saveData()
        val i = Intent(this, ShowProfileActivity::class.java)
        startActivity(i)
        return true
    }

    private fun saveData(){
        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE).edit()
        val user = JSONObject()
        user.put("fullName", findViewById<TextInputEditText>(R.id.fullName_value).text)
        .put("nickname", findViewById<TextInputEditText>(R.id.nickName_value).text)
            .put("description", findViewById<TextInputEditText>(R.id.description_value).text)
            .put("age", findViewById<TextInputEditText>(R.id.gender_value).text)
        .put("gender", findViewById<TextInputEditText>(R.id.gender_value).text)
        .put("location", findViewById<TextInputEditText>(R.id.location_value).text)
        .put("monday", findViewById<TextInputEditText>(R.id.monHours_value).text)
        .put("tuesday", findViewById<TextInputEditText>(R.id.tueHours_value).text)
        .put("wednesday", findViewById<TextInputEditText>(R.id.wedHours_value).text)
        .put("thursday", findViewById<TextInputEditText>(R.id.thuHours_value).text)
        .put("friday", findViewById<TextInputEditText>(R.id.friHours_value).text)
        .put("saturday", findViewById<TextInputEditText>(R.id.satHours_value).text)
        .put("sunday", findViewById<TextInputEditText>(R.id.sunHours_value).text)
        .put("phoneNumber", findViewById<TextInputEditText>(R.id.phoneNumber_value).text)
        .put("email", findViewById<TextInputEditText>(R.id.mail_value).text)

        sp.putString("user", user.toString())
        sp.apply()
    }

    private fun getData(){
        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)

        //extract a json object from a string
        val userString = sp.getString("user", "{\"fullName\":\"Mario Rossi\",\"description\":\"I'm a student\",\"age\":20,\"nickname\":\"m@r1o_ross1\",\"age\":20,\"gender\":\"M\",\"location\":\"Torino\",\"monday\":\"8:00-12:00\",\"tuesday\":\"8:00-12:00\",\"wednesday\":\"8:00-12:00\",\"thursday\":\"8:00-12:00\",\"friday\":\"8:00-12:00\",\"saturday\":\"8:00-12:00\",\"sunday\":\"8:00-12:00\",\"phoneNumber\": \"1234567890\",\"email\": \"mario.rossi@email.it\"}")
        val userObject = JSONObject(userString!!)

        findViewById<TextView>(R.id.fullName_value).text = userObject.getString("fullName")
        findViewById<TextView>(R.id.nickName_value).text = userObject.getString("nickname")
        findViewById<TextView>(R.id.gender_value).text = userObject.getInt("age").toString()
        findViewById<TextView>(R.id.description_value).text = userObject.getString("description")
        findViewById<TextView>(R.id.gender_value).text = userObject.getString("gender")
        findViewById<TextView>(R.id.location_value).text =userObject.getString("location")
        findViewById<TextView>(R.id.monHours_value).text =userObject.getString("monday")
        findViewById<TextView>(R.id.tueHours_value).text =userObject.getString("tuesday")
        findViewById<TextView>(R.id.wedHours_value).text =userObject.getString("wednesday")
        findViewById<TextView>(R.id.thuHours_value).text =userObject.getString("thursday")
        findViewById<TextView>(R.id.friHours_value).text =userObject.getString("friday")
        findViewById<TextView>(R.id.satHours_value).text =userObject.getString("saturday")
        findViewById<TextView>(R.id.sunHours_value).text =userObject.getString("sunday")
        findViewById<TextView>(R.id.mail_value).text =userObject.getString("email")
        findViewById<TextView>(R.id.phoneNumber_value).text =userObject.getString("phoneNumber")

    }
}