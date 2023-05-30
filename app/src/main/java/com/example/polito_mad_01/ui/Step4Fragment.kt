package com.example.polito_mad_01.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.polito_mad_01.R

class Step4Fragment: Fragment(R.layout.step4fragment) {


    private lateinit var mView: View


    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?    ): View {
        mView = inflater.inflate(R.layout.step4fragment, container, false)

        return mView
    }




}