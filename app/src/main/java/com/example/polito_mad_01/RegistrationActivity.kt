package com.example.polito_mad_01

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.aceinteract.android.stepper.StepperNavListener
import com.aceinteract.android.stepper.StepperNavigationView
import com.google.firebase.auth.FirebaseAuth


class RegistrationActivity: AppCompatActivity(), StepperNavListener {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)


        auth = FirebaseAuth.getInstance()

        val stepper = findViewById<StepperNavigationView>(R.id.stepper)
        val navController =
            (supportFragmentManager.findFragmentById(R.id.frame_stepper) as NavHostFragment).navController

        stepper.setupWithNavController(navController)




        findViewById<Button>(R.id.registrationButton).setOnClickListener {

            stepper.goToNextStep()
            onStepChanged(stepper.currentStep + 1)

            findViewById<Button>(R.id.registrationButton2).visibility = View.VISIBLE

            /*val userEmail = findViewById<EditText>(R.id.registrationUsernameEditText).text.toString()
            val userPassword = findViewById<EditText>(R.id.registrationPasswordEditText).text.toString()

            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {
                        // TODO: add user to collection
                    } else {
                        // TODO: navigate to login and message
                    }
                }*/
        }

        findViewById<Button>(R.id.registrationButton2).setOnClickListener {

            stepper.goToPreviousStep()
            onStepChanged(stepper.currentStep + 1)


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