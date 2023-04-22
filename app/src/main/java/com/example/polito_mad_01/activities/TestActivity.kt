package com.example.polito_mad_01.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.viewmodel.TestViewModel
import com.example.polito_mad_01.viewmodel.UserViewModelFactory

class TestActivity : AppCompatActivity() {
    private val vm: TestViewModel by viewModels {
        UserViewModelFactory((application as SportTimeApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database_test)

        findViewById<Button>(R.id.getButton).setOnClickListener() {
            val user = vm.userById(1)
            if(user!= null) {
                findViewById<TextView>(R.id.myText).text = user.name
            }
            else {
                findViewById<TextView>(R.id.myText).text = "User not found"
            }
        }

        vm.users.observe(this) {
            findViewById<TextView>(R.id.myText).text = it.toString()
        }


    }
}