package com.example.polito_mad_01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polito_mad_01.ui.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class RegistrationActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.registrationButton).setOnClickListener{

            var userEmail = findViewById<EditText>(R.id.registrationUsernameEditText).text.toString()
            var userPassword = findViewById<EditText>(R.id.registrationPasswordEditText).text.toString()


            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this,
                    OnCompleteListener<AuthResult?> { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Registration failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Registration ok.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    })
        }

    }
}