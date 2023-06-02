package com.example.polito_mad_01.ui

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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


    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mView = inflater.inflate(R.layout.step2fragment, container, false)
        registrationViewModel = ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBirthdateView(mView)
        setAllView()
        setSpinners()
    }

    private fun setSpinners() {
        val textField = UIUtils.findTextInputById(requireView(),R.id.registrationGenderInputLayout)
        val genderArray = resources.getStringArray(R.array.genderArray)
        println("VALUE $textField")
        val adapter = ArrayAdapter(requireContext(), R.layout.gender_list_item, genderArray)
        (textField?.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setBirthdateView(view: View) {
        val birthdateView = UIUtils.findTextInputById(view,R.id.registrationBirthdayInputLayout)

        val materialDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a Date").build()
        materialDatePicker.addOnPositiveButtonClickListener {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
            birthdateView?.editText?.setText(date)
        }

        birthdateView?.editText?.setOnClickListener {
            materialDatePicker.show(childFragmentManager, "DATE_PICKER")
        }

    }

    private fun setAllView(){
        val name = mView.findViewById<TextInputLayout>(R.id.registrationNameInputLayout)
        name.editText?.setText(registrationViewModel.user.value?.name)
        name.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                registrationViewModel.user.value?.name = s.toString()
            }
        })

        val surname = mView.findViewById<TextInputLayout>(R.id.registrationSurnameInputLayout)
        surname.editText?.setText(registrationViewModel.user.value?.surname)
        surname.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                registrationViewModel.user.value?.surname = s.toString()
            }
        })

        val gender = mView.findViewById<TextInputLayout>(R.id.registrationGenderInputLayout)
        gender.editText?.setText(registrationViewModel.user.value?.gender)
        gender.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                registrationViewModel.user.value?.gender = s.toString()
            }
        })

        val birthdate = mView.findViewById<TextInputLayout>(R.id.registrationBirthdayInputLayout)
        birthdate.editText?.setText(registrationViewModel.user.value?.birthdate)
        birthdate.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                registrationViewModel.user.value?.birthdate = s.toString()
            }
        })

        val location = mView.findViewById<TextInputLayout>(R.id.registrationLocationInputLayout)
        location.editText?.setText(registrationViewModel.user.value?.location)
        location.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                registrationViewModel.user.value?.location = s.toString()
            }
        })

        val nickname = mView.findViewById<TextInputLayout>(R.id.registrationNicknameInputLayout)
                nickname.editText?.setText(registrationViewModel.user.value?.surname)
                nickname.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                registrationViewModel.user.value?.nickname = s.toString()
            }
        })
    }
}