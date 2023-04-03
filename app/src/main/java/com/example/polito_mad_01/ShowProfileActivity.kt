package com.example.polito_mad_01

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.core.net.toUri
import org.json.JSONObject

class ShowProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = when(resources.configuration.orientation){
            Configuration.ORIENTATION_LANDSCAPE -> R.layout.activity_show_profile_landscape
            else -> R.layout.activity_show_profile_portrait
        }

        // calling the action bar
        val actionBar = supportActionBar
        actionBar?.let {it.title = "Profile" }

        setContentView(layout)

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
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
        val userString = sp.getString("user", null)
        userString?.let {
            val userObject = JSONObject(userString)

            setTextView(userObject,"fullName", R.id.fullName_textView)
            setTextView(userObject,"nickname", R.id.nickName_textView)
            setTextView(userObject,"age", R.id.age_textView)
            setTextView(userObject,"description", R.id.description_textView)
            setTextView(userObject,"location", R.id.location_textView)
            setTextView(userObject,"expertList", R.id.expertList_textView)
            setTextView(userObject,"intermediateList", R.id.intermediateList_textView)
            setTextView(userObject,"beginnerList", R.id.beginnerList_textView)
            setTextView(userObject,"monday", R.id.monHours_textView)
            setTextView(userObject,"tuesday", R.id.tueHours_textView)
            setTextView(userObject,"wednesday", R.id.wedHours_textView)
            setTextView(userObject,"thursday", R.id.thuHours_textView)
            setTextView(userObject,"friday", R.id.friHours_textView)
            setTextView(userObject,"saturday", R.id.satHours_textView)
            setTextView(userObject,"sunday", R.id.sunHours_textView)
            setTextView(userObject,"email", R.id.mail_textView)
            setTextView(userObject,"phoneNumber", R.id.phoneNumber_textView)

            val image :String? = userObject.getString("image_data")
            image?.let {
                findViewById<ImageView>(R.id.profileImage_imageView).setImageURI(it.toUri())
            }

            val genderIndex =
                try {userObject.getInt("genderIndex")}
                catch (e: Error) { 0 }

            val gender = resources.getStringArray(R.array.genderArray)[genderIndex]
            findViewById<TextView>(R.id.gender_textView).text = gender

        }
    }
    private fun setTextView(json:JSONObject, key: String, id: Int){
        findViewById<TextView>(id).text = json.getString(key)?: ""
    }


}