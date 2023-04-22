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

        // calling the action bar
        val actionBar = supportActionBar
        actionBar?.let { it.title = "Profile" }
        setContentView(R.layout.show_profile)

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
        ) {
            val permission =
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

    private fun init() {
        try {
            getData()
            setAllView()
        } catch (e: NotImplementedError) {
            e.printStackTrace()
        }
    }

    private fun getData() {
        //get data from database
        //TODO: get data from database
    }

    private fun setAllView() {
        setTextView(R.id.fullName_textView, vm.name.value + " " + vm.surname.value)
        setTextView(R.id.nickName_value, vm.nickname.value)
        setTextView(R.id.description_textView, vm.description.value)
        setTextView(R.id.age_textView, vm.age.value.toString())
        setTextView(R.id.mail_textView, vm.email.value)
        setTextView(R.id.phoneNumber_textView, vm.phoneNumber.value)
        setTextView(R.id.gender_textView, vm.gender.value)
        setTextView(R.id.location_textView, vm.location.value)
        setTextView(R.id.monHours_textView, vm.mondayAvailability.value)
        setTextView(R.id.tueHours_textView, vm.tuesdayAvailability.value)
        setTextView(R.id.wedHours_textView, vm.wednesdayAvailability.value)
        setTextView(R.id.thuHours_textView, vm.thursdayAvailability.value)
        setTextView(R.id.friHours_textView, vm.fridayAvailability.value)
        setTextView(R.id.satHours_textView, vm.saturdayAvailability.value)
        setTextView(R.id.sunHours_textView, vm.sundayAvailability.value)
        setTextView(R.id.expertList_textView, vm.expertList.value)
        setTextView(R.id.intermediateList_textView, vm.intermediateList.value)
        setTextView(R.id.beginnerList_textView, vm.beginnerList.value)
    }

    private fun setTextView(id: Int, field: String?) {
        field?.let { findViewById<TextView>(id).text = field }
    }
}