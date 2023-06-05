package com.example.polito_mad_01

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.polito_mad_01.ui.MainActivity
import com.example.polito_mad_01.viewmodel.*
import com.firebase.ui.auth.*
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.common.SignInButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fs : FirebaseFirestore

    private val vm: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory((application as SportTimeApplication).userRepository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        fs = FirebaseFirestore.getInstance()

        auth.currentUser?.let {
            loginSuccess()
        }

        setupGoogleSignIn()
        setupEmailSignIn()
    }

    private fun loginSuccess(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateRegister(){
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
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

                               Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show()
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
                vm.getUser().observe(this) {
                    if(it == null)
                        navigateRegister()
                    else
                        loginSuccess()

                }
            }
            else Snackbar.make(findViewById(android.R.id.content), idpResponse.error?.message.toString(), Snackbar.LENGTH_LONG).show()
        }
    }

}