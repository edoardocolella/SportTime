package com.example.polito_mad_01.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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


    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.step1fragment, container, false)
        registrationViewModel = ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAllView()
    }

    private fun setAllView(){
        val email = mView.findViewById<TextInputLayout>(R.id.registrationUsername)
        email.editText?.setText(registrationViewModel.user.value?.email)
        email.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                registrationViewModel.user.value?.email = s.toString()
            }
        })

        val password = mView.findViewById<TextInputLayout>(R.id.loginPassword)
        password.editText?.setText(registrationViewModel.user.value?.password)
        password.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                registrationViewModel.user.value?.password = s.toString()
            }
        })
    }
}