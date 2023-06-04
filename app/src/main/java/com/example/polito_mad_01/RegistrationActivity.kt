package com.example.polito_mad_01

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
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
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks.await
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
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
            if (stepper.currentStep == 3) {
                println("STEP 3")
                if (validateStep3())
                    register()
            }
        }

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            var validFlag = true
            when(stepper.currentStep){
                0 -> {
                    println("STEP 0")
                    validFlag = validateStep0()
                }
                1 -> {
                    println("STEP 1")
                    validFlag = validateStep1()
                }
                2 -> {
                    println("STEP 2")
                    validFlag = validateStep2()
                }
            }


            if(validFlag) {
                stepper.goToNextStep()
                //onStepChanged(stepper.currentStep)
                println("STEPPER NUMBER : ${stepper.currentStep}")

                findViewById<Button>(R.id.backButton).visibility = View.VISIBLE

                if (stepper.currentStep == 3) {
                    findViewById<Button>(R.id.nextButton).visibility = View.INVISIBLE
                    findViewById<Button>(R.id.registerButton).visibility = View.VISIBLE
                }
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
            auth.createUserWithEmailAndPassword(vm.user.value!!.email, vm.user.value!!.password)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {

                            vm.createUser(auth.currentUser!!.uid)

                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Registration successful",
                                Snackbar.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }

            }

    }

    override fun onStepChanged(step: Int) {
        //Toast.makeText(this, "Step changed to ${step}", Toast.LENGTH_SHORT).show()
    }

    override fun onCompleted() {
        //Toast.makeText(this, "Stepper completed", Toast.LENGTH_SHORT).show()
    }

    //stepperNavListener = this


    private fun validateStep0() : Boolean {
        val emailInput = findViewById<TextInputLayout>(R.id.registrationUsername)
        val passwordInput = findViewById<TextInputLayout>(R.id.loginPassword)
        if (emailInput.editText?.text!!.isEmpty()) {
            emailInput.error = "Email is empty"
            return false
        } else if (
            !Patterns.EMAIL_ADDRESS.matcher(emailInput.editText?.text!!).matches()) {
            emailInput.error = "Email is not valid"
            return false
        } else {
            emailInput.error = null
        }



        var flag = false
        runBlocking {
            val methods = auth.fetchSignInMethodsForEmail(emailInput.editText?.text.toString())
                .await()
                .signInMethods
            if (methods != null && methods.isNotEmpty()) {
                flag = true
                emailInput.error = "Email already exists"
            }
            else{
                emailInput.error = null
            }
        }
        if(flag) return false


        if (passwordInput.editText?.text!!.length < 6) {
            passwordInput.error = "Password must have at least 6 characters"
            return false
        } else {
            passwordInput.error = null
        }

        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun validateStep1(): Boolean{
        val nameInput = findViewById<TextInputLayout>(R.id.registrationNameInputLayout)
        if(nameInput.editText?.text!!.isEmpty()){
            nameInput.error = "Name is empty"
            return false
        }else{ nameInput.error = null }



        val surnameInput = findViewById<TextInputLayout>(R.id.registrationSurnameInputLayout)
        if(surnameInput.editText?.text!!.isEmpty()){
            surnameInput.error = "Surname is empty"
            return false
        }else{ surnameInput.error = null}

        val nicknameInput = findViewById<TextInputLayout>(R.id.registrationNicknameInputLayout)
        if (nicknameInput.editText?.text!!.isEmpty()){
            nicknameInput.error = "Nickname is empty"
            return false
        }else{ nicknameInput.error = null }

        val achievementInput = findViewById<TextInputLayout>(R.id.registrationAchievementsInputLayout)
        if (achievementInput.editText?.text!!.isEmpty()){
            achievementInput.error = "Achievements are empty"
            return false
        }else{ achievementInput.error = null }

        val genderInput = findViewById<TextInputLayout>(R.id.registrationGenderInputLayout)
        if (genderInput.editText?.text!!.isEmpty()){
            genderInput.error = "Choose an option"
            return false
        }else { genderInput.error = null }

        val birthdateInput = findViewById<TextInputLayout>(R.id.registrationBirthdayInputLayout)
        if(birthdateInput.editText?.text!!.isEmpty()) {
            birthdateInput.error = "Insert your birthdate"
            return false
        }else{ birthdateInput.error = null }

        if(birthdateInput.editText?.text.toString() > LocalDate.now().toString()) {
            birthdateInput.error = "Insert a birthdate in the past"
            return false
        }else{ birthdateInput.error = null }

        val locationInput = findViewById<TextInputLayout>(R.id.registrationLocationInputLayout)
        if(locationInput.editText?.text!!.isEmpty()){
            locationInput.error = "Insert your location"
            return false
        } else{ locationInput.error = null }

        return true
    }

    private fun validateStep2():Boolean{

        val checkBoxBasket = findViewById<CheckBox>(R.id.checkBoxBasket).isChecked
        val checkBoxFootball = findViewById<CheckBox>(R.id.checkBoxFootball).isChecked
        val checkBoxPingPong = findViewById<CheckBox>(R.id.checkBoxPingPong).isChecked
        val checkBoxVolleyball = findViewById<CheckBox>(R.id.checkBoxVolleyball).isChecked

        if(!checkBoxBasket && !checkBoxFootball && !checkBoxPingPong && !checkBoxVolleyball){
            Snackbar.make(findViewById(android.R.id.content), "Select at least one sport", Snackbar.LENGTH_SHORT).show()
            return false
        }

        val basketSkillView = findViewById<AutoCompleteTextView>(R.id.sportLevelBasketMenu).text.toString()
        val footballSkillView = findViewById<AutoCompleteTextView>(R.id.sportLevelFootballMenu).text.toString()
        val pingPongSkillView = findViewById<AutoCompleteTextView>(R.id.sportLevelPingPongMenu).text.toString()
        val volleyballSkillView = findViewById<AutoCompleteTextView>(R.id.sportLevelVolleyballMenu).text.toString()

        if(basketSkillView.isEmpty() && footballSkillView.isEmpty() && pingPongSkillView.isEmpty() && volleyballSkillView.isEmpty()){
            Snackbar.make(findViewById(android.R.id.content), "Select at least one skill", Snackbar.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun validateStep3():Boolean{

        val phonenumberInput = findViewById<TextInputLayout>(R.id.registrationPhoneNumberEditText)
        if(phonenumberInput.editText?.text!!.isEmpty()){
            phonenumberInput.error = "Insert your phone number"
            println("${phonenumberInput.editText?.text!!}")
            return false
        }else{ phonenumberInput.error = null }

        val values = vm.user.value?.availability?.values!!
        if(!values.contains(true)){
            Snackbar.make(findViewById(android.R.id.content), "Select at least one day\"", Snackbar.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}