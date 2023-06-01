package com.example.polito_mad_01.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.R
import com.example.polito_mad_01.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Step1Fragment: Fragment(R.layout.step1fragment) {
    private lateinit var mView: View
    private lateinit var registrationViewModel : RegistrationViewModel

    override fun onStart() {
        super.onStart()
    }

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.step1fragment, container, false)
        registrationViewModel = ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]

        return mView
    }

    override fun onResume() {
        super.onResume()
        setView()
    }

    override fun onStop() {
        super.onStop()
        val email = mView.findViewById<TextInputEditText>(R.id.registrationUsernameEditText)
        val password = mView.findViewById<TextInputEditText>(R.id.registrationPasswordEditText)

        // AUTH parameters
        registrationViewModel.user.value?.email = email.editableText.toString()
        registrationViewModel.user.value?.password = password.editableText.toString()

    }

    private fun setView(){
        mView.findViewById<TextInputLayout>(R.id.registrationUsername).editText?.setText(
            registrationViewModel.user.value?.email ?: ""
        )
        mView.findViewById<TextInputLayout>(R.id.loginPassword).editText?.setText(
            registrationViewModel.user.value?.password ?: ""
        )
    }
}