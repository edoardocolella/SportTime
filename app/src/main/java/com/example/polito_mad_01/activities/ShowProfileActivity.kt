package com.example.polito_mad_01.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.activity.viewModels
import com.example.polito_mad_01.*
import com.example.polito_mad_01.viewmodel.*


class ShowProfileActivity : AppCompatActivity() {

    private val vm : ShowProfileViewModel by viewModels{
        ShowProfileViewModelFactory((application as SportTimeApplication).userRepository)
    }

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

        try {
            setAllView()
        } catch (e: NotImplementedError) {
            e.printStackTrace()
        }
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

    private fun setAllView() {
        vm.getUserWithSkills(1).observe(this) { userWithSkills ->
            userWithSkills.user.let {
                setTextView(R.id.fullName_textView, it.name + " " + it.surname)
                setTextView(R.id.nickName_textView, it.nickname)
                setTextView(R.id.description_textView, it.description)
                setTextView(R.id.age_textView, it.birthdate)
                setTextView(R.id.mail_textView, it.email)
                setTextView(R.id.phoneNumber_textView, it.phoneNumber)
                setTextView(R.id.gender_textView, it.gender)
                setTextView(R.id.location_textView, it.location)
                setTextView(R.id.monHours_textView, it.mondayAvailability)
                setTextView(R.id.tueHours_textView, it.tuesdayAvailability)
                setTextView(R.id.wedHours_textView, it.wednesdayAvailability)
                setTextView(R.id.thuHours_textView, it.thursdayAvailability)
                setTextView(R.id.friHours_textView, it.fridayAvailability)
                setTextView(R.id.satHours_textView, it.saturdayAvailability)
                setTextView(R.id.sunHours_textView, it.sundayAvailability)
            }
            userWithSkills.skills.let { skillList ->
                val expertList =
                    skillList.filter { it.skill_level == "Expert" }.map { it.sportName }
                val intermediateList =
                    skillList.filter { it.skill_level == "Intermediate" }.map { it.sportName }
                val beginnerList =
                    skillList.filter { it.skill_level == "Beginner" }.map { it.sportName }
                setTextView(R.id.expertList_textView, expertList.joinToString(", "))
                setTextView(R.id.intermediateList_textView, intermediateList.joinToString(", "))
                setTextView(R.id.beginnerList_textView, beginnerList.joinToString(", "))
            }
        }
    }

    private fun setTextView(id: Int, field: String?) {
        field?.let { findViewById<TextView>(id).text = field }
    }
}