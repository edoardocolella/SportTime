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
import org.json.JSONException
import org.json.JSONObject

class ShowProfileActivity : AppCompatActivity() {

    private var array = arrayOf(
        Pair("fullName", R.id.fullName_textView),
        Pair("nickname", R.id.nickName_textView),
        Pair("age", R.id.age_textView),
        Pair("description", R.id.description_textView),
        Pair("location", R.id.location_textView),
        Pair("expertList", R.id.expertList_textView),
        Pair("intermediateList", R.id.intermediateList_textView),
        Pair("beginnerList", R.id.beginnerList_textView),
        Pair("monday", R.id.monHours_textView),
        Pair("tuesday", R.id.tueHours_textView),
        Pair("wednesday", R.id.wedHours_textView),
        Pair("thursday", R.id.thuHours_textView),
        Pair("friday", R.id.friHours_textView),
        Pair("saturday", R.id.satHours_textView),
        Pair("sunday", R.id.sunHours_textView),
        Pair("email", R.id.mail_textView),
        Pair("phoneNumber", R.id.phoneNumber_textView)
    )

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

            array.forEach {
                val view = findViewById<TextView>(it.second)
                val text = try {
                    userObject.getString(it.first)
                }catch (e: JSONException){
                    ""
                }
                view.text = text
            }

            try {
                val image :String = userObject.getString("image_data")
                println(" IMAGE STRING $image" )
                println(" IMAGE URI ${image.toUri()}")
                if (image.isNotEmpty())
                    findViewById<ImageView>(R.id.profileImage_imageView).setImageURI(image.toUri())
            }
            catch (_: JSONException) {println("NO IMAGE") }

            val genderIndex =
                try {userObject.getInt("genderIndex")}
                catch (e: Error) { 0 }

            val gender = resources.getStringArray(R.array.genderArray)[genderIndex]
            findViewById<TextView>(R.id.gender_textView).text = gender

        }
    }
}