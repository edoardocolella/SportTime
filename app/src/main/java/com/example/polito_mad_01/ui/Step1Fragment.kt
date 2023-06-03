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
    private lateinit var vm : RegistrationViewModel
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.step1fragment, container, false)
        vm = ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailInputLayout = mView.findViewById(R.id.registrationUsername)
        passwordInputLayout = mView.findViewById(R.id.loginPassword)
    }

    override fun onResume() {
        super.onResume()
        setAllView()
    }

    override fun onStop() {
        super.onStop()
        vm.user.value?.email = emailInputLayout.editText?.text.toString()
        vm.user.value?.password = passwordInputLayout.editText?.text.toString()
    }

    private fun setAllView() {
        emailInputLayout.editText?.setText(vm.user.value?.email ?: "")
        passwordInputLayout.editText?.setText(vm.user.value?.password ?: "")
    }
}