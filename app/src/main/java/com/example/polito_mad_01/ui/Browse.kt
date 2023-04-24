package com.example.polito_mad_01.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.polito_mad_01.R

class Browse : Fragment(R.layout.fragment_browse) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.title = "Browse"
    }
}