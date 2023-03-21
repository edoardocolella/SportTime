package com.example.polito_mad_01

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import kotlin.math.roundToInt

class ShowProfileActivity : AppCompatActivity() {
    private fun fromDpToPx(pxValue: Int) : Int {
        val density: Float = resources.displayMetrics.density
        return (pxValue*density).roundToInt()
    }

    private fun adaptOrientation() {
        val appLayout: LinearLayout = findViewById(R.id.appLayout)

        when(resources.configuration.orientation){
            Configuration.ORIENTATION_LANDSCAPE -> {
                appLayout.orientation = LinearLayout.HORIZONTAL
            }
            else -> {
                appLayout.orientation = LinearLayout.VERTICAL
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)

        adaptOrientation()
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
        val sp = getSharedPreferences("mySharedPreferencies", Context.MODE_PRIVATE)
        val fullName = sp.getString("fullName", "Mario Rossi")
        val nickname = sp.getString("nickname", "m@r1o_ross1")
        val age = sp.getInt("age", 20)
        val expertSports = sp.getStringSet("expert", mutableSetOf())
        val intermediateSports = sp.getStringSet("intermediate", mutableSetOf())
        val beginnerSports = sp.getStringSet("beginner", mutableSetOf())
        val gender = sp.getString("gender", "M")
        val location = sp.getString("location", "Torino")
        val phoneNumber = sp.getString("phoneNumber", "1234567890")
        val email = sp.getString("email", "mario.rossi@email.it")
        val mon = sp.getString("monday", "10.00 - 20.00")
        val tue = sp.getString("tuesday", "10.00 - 20.00")
        val wed = sp.getString("wednesday", "10.00 - 20.00")
        val thu = sp.getString("thursday", "10.00 - 20.00")
        val fri = sp.getString("friday", "10.00 - 20.00")
        val sat = sp.getString("saturday", "10.00 - 20.00")
        val sun = sp.getString("sunday", "10.00 - 20.00")
    }


}