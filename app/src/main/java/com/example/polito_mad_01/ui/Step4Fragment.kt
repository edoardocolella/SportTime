package com.example.polito_mad_01.ui

import android.annotation.SuppressLint
import android.os.*
import android.text.*
import android.view.*
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.R
import com.example.polito_mad_01.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.TextInputLayout

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationViewModel = ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]
        registrationViewModel.user.value?.availability = availabilityMap
        setButtons()
        setPhoneNumber()
    }

    private fun setPhoneNumber() {
        val phoneNumber = mView.findViewById<TextInputLayout>(R.id.registrationPhoneNumberEditText)
        phoneNumber.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                registrationViewModel.user.value?.phoneNumber = s.toString()
            }
        })
    }

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.step4fragment, container, false)

        return mView
    }

    private fun setButtons() {
        setButtonAndListener(R.id.mondayButton, getAvailability("monday"), "monday")
        setButtonAndListener(R.id.tuesdayButton, getAvailability("tuesday"), "tuesday")
        setButtonAndListener(R.id.wednesdayButton,getAvailability("wednesday"), "wednesday")
        setButtonAndListener(R.id.thursdayButton, getAvailability("thursday"), "thursday")
        setButtonAndListener(R.id.fridayButton, getAvailability("friday"), "friday")
        setButtonAndListener(R.id.saturdayButton, getAvailability("saturday"), "saturday")
        setButtonAndListener(R.id.sundayButton, getAvailability("sunday"), "sunday")
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
        registrationViewModel.user.value?.availability?.set(attribute, checked)
    }

    private fun getAvailability(attribute: String): Boolean {
        return registrationViewModel.user.value?.availability?.get(attribute)!!
    }
}