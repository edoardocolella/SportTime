package com.example.polito_mad_01

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class EditProfileActivity : AppCompatActivity() {

    var frame: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_portrait)
        //frame = findViewById(R.id.imageView)
        getData()

        //checkPermissionCamera
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, 112)
            }
        }

        frame?.setOnLongClickListener(View.OnLongClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_DENIED
                ) {
                    val permission = arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestPermissions(permission, 121)
                } else {
                    //error
                }
            } else {
                //error
            }
            openCamera()
            true
        })
        */
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