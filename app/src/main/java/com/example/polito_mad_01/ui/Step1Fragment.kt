package com.example.polito_mad_01.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.polito_mad_01.R
import com.google.android.material.chip.ChipGroup

class Step1Fragment: Fragment(R.layout.step1fragment) {

    private lateinit var mView: View


    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.step1fragment, container, false)

        return mView
    }
}