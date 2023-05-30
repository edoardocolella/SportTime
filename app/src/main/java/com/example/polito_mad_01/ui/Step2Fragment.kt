package com.example.polito_mad_01.ui

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.R
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.RegistrationViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class Step2Fragment: Fragment(R.layout.step2fragment) {
    private lateinit var mView: View
    private lateinit var registrationViewModel : RegistrationViewModel

    override fun onStart() {
        super.onStart()
        registrationViewModel = ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]
    }


    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.step2fragment, container, false)

        setBirthdateView(mView)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinners()
    }
    private fun setSpinners() {
        val textField = UIUtils.findTextInputById(requireView(),R.id.registrationGenderInputLayout)
        val genderArray = resources.getStringArray(R.array.genderArray)
        println("VALUE ${textField}")
        val adapter = ArrayAdapter(requireContext(), R.layout.gender_list_item, genderArray)
        (textField?.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setBirthdateView(view: View) {
        val birthdateView = UIUtils.findTextInputById(view,R.id.registrationBirthdayInputLayout)
       //birthdateView?.editText?.setText(user.birthdate)

        val materialDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a Date").build()
        materialDatePicker.addOnPositiveButtonClickListener {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
            birthdateView?.editText?.setText(date)
            //setValue("birthdate", date)
        }

        birthdateView?.editText?.setOnClickListener {
            materialDatePicker.show(childFragmentManager, "DATE_PICKER")
        }

    }

    override fun onStop() {
        super.onStop()

        val name = mView.findViewById<TextInputEditText>(R.id.registrationNameEditText)
        val surname = mView.findViewById<TextInputEditText>(R.id.registrationSurnameEditText)
        val gender = mView.findViewById<TextInputLayout>(R.id.registrationGenderInputLayout)
        val birthdate = mView.findViewById<TextInputLayout>(R.id.registrationBirthdayInputLayout)
        val location = mView.findViewById<TextInputEditText>(R.id.registrationLocationEditText)
        val nickname = mView.findViewById<TextInputEditText>(R.id.registrationNicknameEditText)

        registrationViewModel.user.value?.name = name.editableText.toString()
        registrationViewModel.user.value?.surname = surname.editableText.toString()
        registrationViewModel.user.value?.gender = gender.editText?.text.toString()
        registrationViewModel.user.value?.birthdate = birthdate.editText?.text.toString()
        registrationViewModel.user.value?.location = location.editableText.toString()
        registrationViewModel.user.value?.nickname = nickname.editableText.toString()

    }
}