package com.example.polito_mad_01

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import org.json.JSONObject

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        getData()
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
        user.put("fullName", findViewById<TextView>(R.id.fullName_inputView).text)
        .put("nickname", findViewById<TextView>(R.id.nickName_inputLayout).text)
        .put("age", findViewById<TextView>(R.id.age_inputLayout).text)
        .put("gender", findViewById<TextView>(R.id.gender_inputView).text)
        .put("location", findViewById<TextView>(R.id.location_inputView).text)
        .put("monday", findViewById<TextView>(R.id.monHours_inputView).text)
        .put("tuesday", findViewById<TextView>(R.id.tueHours_inputView).text)
        .put("wednesday", findViewById<TextView>(R.id.wedHours_inputView).text)
        .put("thursday", findViewById<TextView>(R.id.thuHours_inputView).text)
        .put("friday", findViewById<TextView>(R.id.friHours_inputView).text)
        .put("saturday", findViewById<TextView>(R.id.satHours_inputView).text)
        .put("sunday", findViewById<TextView>(R.id.sunHours_inputView).text)
        .put("phoneNumber", findViewById<TextView>(R.id.phoneNumber_inputView).text)
        .put("email", findViewById<TextView>(R.id.mail_inputView).text)

        sp.putString("user", user.toString())
        sp.apply()
    }

    private fun getData(){
        val sp = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)

        //extract a json object from a string
        val userString = sp.getString("user", "{\"fullname\":\"Mario Rossi\",\"nickname\":\"m@r1o_ross1\",\"age\":20,\"gender\":\"M\",\"location\":\"Torino\",\"monday\":\"8:00-12:00\",\"tuesday\":\"8:00-12:00\",\"wednesday\":\"8:00-12:00\",\"thursday\":\"8:00-12:00\",\"friday\":\"8:00-12:00\",\"saturday\":\"8:00-12:00\",\"sunday\":\"8:00-12:00\",\"phoneNumber\": \"1234567890\",\"email\": \"mario.rossi@email.it\"}")
        val userObject = JSONObject(userString!!)

        findViewById<TextView>(R.id.fullName_inputView).text = userObject.getString("fullName")
        findViewById<TextView>(R.id.nickName_inputLayout).text = userObject.getString("nickname")
        findViewById<TextView>(R.id.age_inputLayout).text = userObject.getInt("age").toString()
        findViewById<TextView>(R.id.description_inputView).text = userObject.getString("description")
        findViewById<TextView>(R.id.gender_inputView).text = userObject.getString("gender")
        findViewById<TextView>(R.id.location_inputView).text =userObject.getString("location")
        findViewById<TextView>(R.id.monHours_inputView).text =userObject.getString("monday")
        findViewById<TextView>(R.id.tueHours_inputView).text =userObject.getString("tuesday")
        findViewById<TextView>(R.id.wedHours_inputView).text =userObject.getString("wednesday")
        findViewById<TextView>(R.id.thuHours_inputView).text =userObject.getString("thursday")
        findViewById<TextView>(R.id.friHours_inputView).text =userObject.getString("friday")
        findViewById<TextView>(R.id.satHours_inputView).text =userObject.getString("saturday")
        findViewById<TextView>(R.id.sunHours_inputView).text =userObject.getString("sunday")
        findViewById<TextView>(R.id.mail_inputView).text =userObject.getString("email")
        findViewById<TextView>(R.id.phoneNumber_inputView).text =userObject.getString("phoneNumber")

    }
}