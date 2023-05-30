package com.example.polito_mad_01.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.LoginActivity
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Step4Fragment: Fragment(R.layout.step4fragment) {
    private lateinit var mView: View
    private lateinit var registrationViewModel : RegistrationViewModel

    private val availabilityMap = mutableMapOf(
        "monday" to false,
        "tuesday" to false,
        "wednesday" to false,
        "thursday" to false,
        "friday" to false,
        "saturday" to false,
        "sunday" to false,
    )

    override fun onStart() {
        super.onStart()
        registrationViewModel = ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtons()
    }

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.step4fragment, container, false)

        return mView
    }

    private fun setButtons() {
        setButtonAndListener(R.id.mondayButton, availabilityMap["monday"]!!, "monday")
        setButtonAndListener(R.id.tuesdayButton, availabilityMap["tuesday"]!!, "tuesday")
        setButtonAndListener(R.id.wednesdayButton,availabilityMap["wednesday"]!!, "wednesday")
        setButtonAndListener(R.id.thursdayButton, availabilityMap["thursday"]!!, "thursday")
        setButtonAndListener(R.id.fridayButton, availabilityMap["friday"]!!, "friday")
        setButtonAndListener(R.id.saturdayButton, availabilityMap["saturday"]!!, "saturday")
        setButtonAndListener(R.id.sundayButton, availabilityMap["sunday"]!!, "sunday")
    }

    private fun setButtonAndListener(id: Int, value: Boolean, attribute: String) {
        val button = requireView().findViewById<Button>(id)
        setButtonColor(value, button)
        button.setOnClickListener {
            val newValue = !getAvailability(attribute)
            setAvailability(attribute, newValue)
            setButtonColor(newValue, button)
        }
    }

    private fun setButtonColor(value: Boolean, button: Button) {
        val colorTrue = ContextCompat.getColor(requireContext(), R.color.powder_blue)
        val colorFalse = ContextCompat.getColor(requireContext(), R.color.gray)

        if (value) button.setBackgroundColor(colorTrue)
        else button.setBackgroundColor(colorFalse)
    }

    private fun setAvailability(attribute: String, checked: Boolean) {
        availabilityMap[attribute] = checked
    }

    private fun getAvailability(attribute: String): Boolean {
        return availabilityMap[attribute]!!
    }

    override fun onStop() {
        super.onStop()

        val phoneNumber = mView.findViewById<TextInputEditText>(R.id.registrationPhoneNumberEditText)

        registrationViewModel.user.value?.phoneNumber = phoneNumber.text.toString()
        registrationViewModel.user.value?.availability = mutableMapOf(
            "monday" to true,
            "tuesday" to true,
            "wednesday" to true,
            "thursday" to true,
            "friday" to true,
            "saturday" to true,
            "sunday" to true,
        )
    }
}