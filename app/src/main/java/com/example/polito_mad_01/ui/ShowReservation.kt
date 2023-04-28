package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.polito_mad_01.R
class ShowReservation : Fragment(R.layout.fragment_show_reservation) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.playgroundName).text = requireArguments().getInt("slotID").toString()
    }
}