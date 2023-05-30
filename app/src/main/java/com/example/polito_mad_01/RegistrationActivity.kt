package com.example.polito_mad_01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.aceinteract.android.stepper.StepperNavListener
import com.aceinteract.android.stepper.StepperNavigationView
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.ui.MainActivity
import com.example.polito_mad_01.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class RegistrationActivity: AppCompatActivity(), StepperNavListener {
    private lateinit var auth: FirebaseAuth
    //private val vm : RegistrationViewModel by activityViewModels()

    private lateinit var vm : RegistrationViewModel
    override fun onStart() {
        super.onStart()
        vm = ViewModelProvider(this)[RegistrationViewModel::class.java]
    }


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

        findViewById<Button>(R.id.registerButton).setOnClickListener {
            register()
        }

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            stepper.goToNextStep()
            //onStepChanged(stepper.currentStep)
            println("STEPPER NUMBER : ${stepper.currentStep}")

            findViewById<Button>(R.id.backButton).visibility = View.VISIBLE

            if(stepper.currentStep == 3){
                findViewById<Button>(R.id.nextButton).visibility = View.INVISIBLE
                findViewById<Button>(R.id.registerButton).visibility = View.VISIBLE
            }
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
                findViewById<Button>(R.id.registerButton).visibility = View.INVISIBLE
            }

        }

}
    private fun register() {
        vm.user.observe(this) {
            auth.createUserWithEmailAndPassword(vm.user.value!!.email, vm.user.value!!.password)
                .addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {
                        // TODO: add user to collection
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        println("ERROREERRORE")
                    }
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