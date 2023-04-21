package com.example.polito_mad_01.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.activity.viewModels
import com.example.polito_mad_01.R
import com.example.polito_mad_01.viewmodel.ShowProfileViewModel

class ShowProfileActivity : AppCompatActivity() {

    private val vm by viewModels<ShowProfileViewModel>()

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

        init()

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

    private fun init(){
        try {
            getData()
            setAllView()
        }
        catch (e: NotImplementedError){
            e.printStackTrace()
        }
    }

    private fun getData(){
        //get data from database
        throw NotImplementedError()
    }

    private fun setAllView(){
        /*val user = vm.user.value!!.user
        val list = vm.user.value!!.sportAvailabilityList
        //val expertList = list.filter { it.skill == "Expert" }
        //val intermediateList = list.filter { it.skill == "Intermediate" }
        //val beginnerList = list.filter { it.skill == "Beginner" }
        setTextView(R.id.fullName_textView , user.fullname)
        setTextView(R.id.nickName_value , user.nickname)
        setTextView(R.id.description_textView , user.description)
        setTextView(R.id.age_textView , user.age.toString())
        setTextView(R.id.mail_textView , user.email)
        setTextView(R.id.phoneNumber_textView , user.phoneNumber)
        setTextView(R.id.gender_textView, user.gender)
        setTextView(R.id.location_textView , user.location)
        setTextView(R.id.monHours_textView, user.mondayAvailability)
        setTextView(R.id.tueHours_textView, user.tuesdayAvailability)
        setTextView(R.id.wedHours_textView, user.wednesdayAvailability)
        setTextView(R.id.thuHours_textView, user.thursdayAvailability)
        setTextView(R.id.friHours_textView, user.fridayAvailability)
        setTextView(R.id.satHours_textView, user.saturdayAvailability)
        setTextView(R.id.sunHours_textView, user.sundayAvailability)
        //setTextView(R.id.expertList_textView, expertList.toString())
        //setTextView(R.id.intermediateList_textView, intermediateList.toString())
        //setTextView(R.id.beginnerList_textView, beginnerList.toString())*/
    }

    private fun setTextView(id: Int, field: String){
        findViewById<TextView>(id).text = field
    }
}