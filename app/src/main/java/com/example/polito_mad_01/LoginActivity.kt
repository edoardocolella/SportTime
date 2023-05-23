package com.example.polito_mad_01

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polito_mad_01.ui.MainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
        ) {
            val permission =
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, 112)
        }

        setupGoogleSignIn()
        setupEmailSignIn()
    }

    private fun loginSuccess(){
        val userId = auth.currentUser?.uid

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }

    private fun navigateRegister(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setupRegister(){
        findViewById<Button>(R.id.registerButton).setOnClickListener {
            navigateRegister()
        }
    }

    private fun setupEmailSignIn(){
        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val email = findViewById<EditText>(R.id.loginUsernameEditText).text.toString()
            val password = findViewById<EditText>(R.id.loginPasswordEditText).text.toString()

            // TODO: validation email e password

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        loginSuccess()
                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthException) {
                            val errorMessage = when(e){
                                is FirebaseAuthInvalidCredentialsException -> "Invalid password"
                                is FirebaseAuthInvalidUserException -> "No user found"
                                else -> "Some error occured: ${e.errorCode}"
                            }

                            Toast.makeText(applicationContext,
                                errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
        }
    }

    private fun setupGoogleSignIn(){
        val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract(),
        ) { res ->
            this.onSignInResult(res)
        }

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        findViewById<Button>(R.id.googleButton).setOnClickListener {
            signInLauncher.launch(signInIntent)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // TODO: if user collections contains userId then go login, else create
            loginSuccess()
        } else {
            if (response != null) {
                Toast.makeText(this, response.error?.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

}