package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.viewmodel.*

class EditReservation : Fragment(R.layout.fragment_edit_reservation) {

    private val vm: EditReservationViewModel by viewModels {
        EditReservationViewModelFactory((activity?.application as SportTimeApplication).reservationRepository)
    }

    override fun onCreateView(inflater: LayoutInflater,        container: ViewGroup?,        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setAllView(){
        val id = arguments?.getInt("reservationId")?:1
        vm.getReservation(id).observe(viewLifecycleOwner)
        {
            println(it)
        }
    }

}