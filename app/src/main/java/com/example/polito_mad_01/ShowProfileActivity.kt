package com.example.polito_mad_01

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import org.json.JSONObject

class ShowProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = when(resources.configuration.orientation){
            Configuration.ORIENTATION_LANDSCAPE -> R.layout.activity_show_profile_landscape
            else -> R.layout.activity_show_profile_portrait
        }

        // calling the action bar
        var actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Profile"
        }

        setContentView(layout)

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED) {
            val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission, 112)
        }

        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_show_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = Intent(this, EditProfileActivity::class.java)
        startActivity(i)
        return true
    }

    private fun getData(){
        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)

        //extract a json object from a string
        val userString = sp.getString("user", "{\"fullName\":\"Mario Rossi\",\"nickname\":\"C1cal0ne\",\"description\":\"Best striker Cenisia\",\"age\":34,\"gender\":\"M\",\"location\":\"Torino\",\"monday\":\"8:00-12:00\",\"tuesday\":\"8:00-12:00\",\"wednesday\":\"8:00-12:00\",\"thursday\":\"8:00-12:00\",\"friday\":\"8:00-12:00\",\"saturday\":\"8:00-12:00\",\"sunday\":\"8:00-12:00, 16:00-20:00\",\"phoneNumber\": \"1234567890\",\"email\": \"mario.rossi@email.it\"}")
        val userObject = JSONObject(userString!!)

        findViewById<TextView>(R.id.fullName_textView2).text = userObject.getString("fullName")
        findViewById<TextView>(R.id.nickName_textView).text = userObject.getString("nickname")
        findViewById<TextView>(R.id.age_textView).text = userObject.getString("age")
        findViewById<TextView>(R.id.description_textView).text = userObject.getString("description")
        findViewById<TextView>(R.id.gender_textView).text = userObject.getString("gender")
        findViewById<TextView>(R.id.location_textView).text =userObject.getString("location")
        findViewById<TextView>(R.id.monHours_textView).text =userObject.getString("monday")
        findViewById<TextView>(R.id.tueHours_textView).text =userObject.getString("tuesday")
        findViewById<TextView>(R.id.wedHours_textView).text =userObject.getString("wednesday")
        findViewById<TextView>(R.id.thuHours_textView).text =userObject.getString("thursday")
        findViewById<TextView>(R.id.friHours_textView).text =userObject.getString("friday")
        findViewById<TextView>(R.id.satHours_textView).text =userObject.getString("saturday")
        findViewById<TextView>(R.id.sunHours_textView).text =userObject.getString("sunday")
        findViewById<TextView>(R.id.mail_textView).text =userObject.getString("email")
        findViewById<TextView>(R.id.phoneNumber_textView).text =userObject.getString("phoneNumber")
        /*var test :String? = userObject.getString("image_data")
        val b: ByteArray = Base64.decode(test, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
        findViewById<ImageView>(R.id.profileImage_imageView).setImageBitmap(bitmap)*/
    }


}