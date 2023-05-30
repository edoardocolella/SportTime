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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.LoginActivity
import com.example.polito_mad_01.R
import com.example.polito_mad_01.viewmodel.RegistrationViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Step4Fragment: Fragment(R.layout.step4fragment) {
    private lateinit var mView: View
    private lateinit var registrationViewModel : RegistrationViewModel

    override fun onStart() {
        super.onStart()
        registrationViewModel = ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]
    }

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.step4fragment, container, false)

        return mView
    }

    override fun onStop() {
        super.onStop()

        val phoneNumber = mView.findViewById<TextInputEditText>(R.id.registrationPhoneNumberEditText)

        registrationViewModel.user.value?.phoneNumber = phoneNumber.text.toString()
        registrationViewModel.user.value?.availability = mutableMapOf(
            "Monday" to true,
            "Tuesday" to true,
            "Wednesday" to true,
            "Thursday" to true,
            "Friday" to true,
            "Saturday" to true,
            "Sunday" to true,
        )
    }
}