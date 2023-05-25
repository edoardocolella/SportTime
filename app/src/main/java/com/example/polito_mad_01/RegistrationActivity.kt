package com.example.polito_mad_01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.polito_mad_01.viewmodel.MainActivityViewModel
import com.example.polito_mad_01.viewmodel.MainActivityViewModelFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class RegistrationActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private val vm: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory((application as SportTimeApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.registrationBackButton).setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // TODO: hide or skip email/password part when user logs in with google
        auth.currentUser?.let {
            findViewById<EditText>(R.id.registrationUsernameEditText).setText(it.email)
            findViewById<EditText>(R.id.registrationUsernameEditText).isEnabled = false
            findViewById<EditText>(R.id.registrationPasswordEditText).isEnabled = false
            findViewById<EditText>(R.id.loginConfirmPasswordEditText).isEnabled = false
        }

        findViewById<Button>(R.id.registrationButton).setOnClickListener{

            val userEmail = findViewById<EditText>(R.id.registrationUsernameEditText).text.toString()
            val userPassword = findViewById<EditText>(R.id.registrationPasswordEditText).text.toString()

            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {
                        // TODO: add user to collection
                    } else {
                        Toast.makeText(applicationContext,
                            "Registration error",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
        }

    }
}