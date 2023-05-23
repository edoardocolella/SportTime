package com.example.polito_mad_01

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class RegistrationActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.registrationButton).setOnClickListener{

            val userEmail = findViewById<EditText>(R.id.registrationUsernameEditText).text.toString()
            val userPassword = findViewById<EditText>(R.id.registrationPasswordEditText).text.toString()

            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {
                        // TODO: add user to collection
                    } else {
                        // TODO: navigate to login and message
                    }
                }
        }

    }
}