package com.example.polito_mad_01.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.R
import com.example.polito_mad_01.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

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

        return mView
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
        registrationViewModel.user.value?.gender = gender.editText.toString()
        registrationViewModel.user.value?.birthdate = birthdate.editText.toString()
        registrationViewModel.user.value?.location = location.editableText.toString()
        registrationViewModel.user.value?.nickname = nickname.editableText.toString()

    }
}