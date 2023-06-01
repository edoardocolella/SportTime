package com.example.polito_mad_01

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.aceinteract.android.stepper.StepperNavListener
import com.aceinteract.android.stepper.StepperNavigationView
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.model.UserData
import com.example.polito_mad_01.repositories.UserRepository
import com.example.polito_mad_01.ui.MainActivity
import com.example.polito_mad_01.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate


class RegistrationActivity: AppCompatActivity(), StepperNavListener {
    private lateinit var auth: FirebaseAuth
    //private val vm : RegistrationViewModel by activityViewModels()

    private lateinit var vm : RegistrationViewModel
    override fun onStart() {
        super.onStart()
        vm = ViewModelProvider(this)[RegistrationViewModel::class.java]

        if(auth.currentUser != null){
            findViewById<StepperNavigationView>(R.id.stepper).goToNextStep()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val stepper = findViewById<StepperNavigationView>(R.id.stepper)
        val navController =
            (supportFragmentManager.findFragmentById(R.id.frame_stepper) as NavHostFragment).navController

        stepper.setupWithNavController(navController)

        auth = FirebaseAuth.getInstance()

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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun register() {

        vm.user.observe(this) {
            if(auth.currentUser != null){
                println("USER MAIL ")
                it.email = auth.currentUser!!.email!!
                vm.createUser(auth.currentUser!!.uid)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                return@observe
            }
            if(isValid(it)) {
                auth.createUserWithEmailAndPassword(vm.user.value!!.email, vm.user.value!!.password)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {

                            vm.createUser(auth.currentUser!!.uid)

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }
            }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isValid(user: UserData):Boolean{
        if(user.name.isEmpty()) {
            Toast.makeText(this, "Insert your name", Toast.LENGTH_SHORT).show()
            return false }
        if(user.surname.isEmpty()) {
            Toast.makeText(this, "Insert your surname", Toast.LENGTH_SHORT).show()
            return false }
        if(user.nickname.isEmpty()) {
            Toast.makeText(this, "Insert your nickname", Toast.LENGTH_SHORT).show()
            return false }
        if(user.birthdate.isEmpty()) {
            Toast.makeText(this, "Insert your birthdate", Toast.LENGTH_SHORT).show()
            return false        }

        if(user.birthdate > LocalDate.now().toString()) {
            Toast.makeText(this, "Insert a birthdate in the past", Toast.LENGTH_SHORT).show()
            return false }


        if(user.gender.isEmpty()) {
            Toast.makeText(this, "Insert your gender", Toast.LENGTH_SHORT).show()
            return false }
        if(user.location.isEmpty()) {
            Toast.makeText(this, "Insert your location", Toast.LENGTH_SHORT).show()
            return false}

        if(user.email.isEmpty()) {
            Toast.makeText(this, "Insert your email", Toast.LENGTH_SHORT).show()
            return false }

        if(user.password.isEmpty() || user.password.length<6){
            Toast.makeText(this, "Insert a password longer than 6 characters", Toast.LENGTH_SHORT).show()
            return false}

        if(user.phoneNumber.isEmpty()){
            Toast.makeText(this, "Insert a phone number", Toast.LENGTH_SHORT).show()
            return false}

        if(user.phoneNumber.length<10) {
            Toast.makeText(this, "Phone number must have at least 10 digits", Toast.LENGTH_SHORT).show()
            return false }

        return true
    }

    override fun onStepChanged(step: Int) {
        //Toast.makeText(this, "Step changed to ${step}", Toast.LENGTH_SHORT).show()
    }

    override fun onCompleted() {
        //Toast.makeText(this, "Stepper completed", Toast.LENGTH_SHORT).show()
    }

    //stepperNavListener = this
}