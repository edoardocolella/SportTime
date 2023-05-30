package com.example.polito_mad_01

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.aceinteract.android.stepper.StepperNavListener
import com.aceinteract.android.stepper.StepperNavigationView
import com.example.polito_mad_01.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class RegistrationActivity: AppCompatActivity(), StepperNavListener {
    private lateinit var auth: FirebaseAuth
    private val registrationViewModel : RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val stepper = findViewById<StepperNavigationView>(R.id.stepper)
        val navController =
            (supportFragmentManager.findFragmentById(R.id.frame_stepper) as NavHostFragment).navController

        stepper.setupWithNavController(navController)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            stepper.goToNextStep()
        }

/*        findViewById<Button>(R.id.registrationBackButton).setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }*/

        // TODO: hide or skip email/password part when user logs in with google
/*        auth.currentUser?.let {
            findViewById<EditText>(R.id.registrationUsernameEditText).setText(it.email)
            findViewById<EditText>(R.id.registrationUsernameEditText).isEnabled = false
            findViewById<EditText>(R.id.registrationPasswordEditText).isEnabled = false
            findViewById<EditText>(R.id.loginConfirmPasswordEditText).isEnabled = false
        }*/



        findViewById<Button>(R.id.nextButton).setOnClickListener {

            when(stepper.currentStep){
                0 -> {
                    val email = findViewById<TextInputEditText>(R.id.registrationUsernameEditText)
                    findViewById<Button>(R.id.nextButton).setOnClickListener {
                        registrationViewModel.user.value?.email = email.editableText.toString()
                        Log.v("step1","-----------------------${registrationViewModel.user.value?.email}-----------------------")
                    }
                }
                1 -> {}
                2 -> {}
                3 -> {}
            }

            stepper.goToNextStep()
            //onStepChanged(stepper.currentStep)
            println("STEPPER NUMBER : ${stepper.currentStep}")

            findViewById<Button>(R.id.backButton).visibility = View.VISIBLE

            if(stepper.currentStep == 3){
                findViewById<Button>(R.id.nextButton).visibility = View.INVISIBLE
            }

            /*val userEmail = findViewById<EditText>(R.id.registrationUsernameEditText).text.toString()
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
                }*/
        }

        findViewById<Button>(R.id.backButton).setOnClickListener {

            if(stepper.currentStep == 1) {
                stepper.goToPreviousStep()
                onStepChanged(stepper.currentStep)
                findViewById<Button>(R.id.backButton).visibility = View.INVISIBLE
            }else{
                stepper.goToPreviousStep()
                onStepChanged(stepper.currentStep)
            }

            if(stepper.currentStep == 2){
                findViewById<Button>(R.id.nextButton).visibility = View.VISIBLE
            }

        }

}

    override fun onStepChanged(step: Int) {
        Toast.makeText(this, "Step changed to ${step}", Toast.LENGTH_SHORT).show()
    }

    override fun onCompleted() {
        Toast.makeText(this, "Stepper completed", Toast.LENGTH_SHORT).show()
    }

    //stepperNavListener = this
}