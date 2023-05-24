package com.example.polito_mad_01

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polito_mad_01.repositories.UserRepository
import com.example.polito_mad_01.ui.MainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    val userRepository = UserRepository()
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

        auth.currentUser?.let {
            loginSuccess()
        }

        setupGoogleSignIn()
        setupEmailSignIn()
        setupRegister()
    }

    private fun loginSuccess(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateRegister(){
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    private fun setupRegister(){
        findViewById<Button>(R.id.registerButton).setOnClickListener {
            navigateRegister()
        }
    }

    private fun setupEmailSignIn(){
        val emailField = findViewById<TextInputLayout>(R.id.loginUsername)
        val passwordField = findViewById<TextInputLayout>(R.id.loginPassword)

        emailField.setOnFocusChangeListener { _, _ ->
            emailField.error = ""
        }

        passwordField.setOnFocusChangeListener { _, _ ->
            passwordField.error = ""
        }

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            emailField.error = ""
            passwordField.error = ""

            val email = emailField.editText?.text.toString()
            val password = passwordField.editText?.text.toString()

            if(email.isNotBlank() && password.isNotBlank()){
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            loginSuccess()
                        } else {
                            try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthException) {
                                val errorMessage = when(e){
                                    is FirebaseAuthInvalidCredentialsException -> {
                                        passwordField.error = "Invalid password"
                                        "Invalid password"
                                    }
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
            } else {
                if(email.isBlank())
                    emailField.error = "Insert valid email"
                if(password.isBlank())
                    passwordField.error = "Insert valid password"
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

        findViewById<SignInButton>(R.id.googleButton).setOnClickListener {
            signInLauncher.launch(signInIntent)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        result.idpResponse?.let { idpResponse ->
            if(result.resultCode == RESULT_OK){
                userRepository.getUser(auth.currentUser?.uid ?: "").observe(this) {
                    if(it == null)
                        navigateRegister()
                    else
                        loginSuccess()

                }
            } else {
                Toast.makeText(this, idpResponse.error?.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

}