package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.adapters.ReservationAdapter
import com.example.polito_mad_01.model.*

class ReservationList(private val slotList: List<Slot> = listOf()) : Fragment(R.layout.fragment_reservation_list) {
    private lateinit var reservationsView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reservationsView = view.findViewById(R.id.reservationList)
        reservationsView.layoutManager = LinearLayoutManager(view.context)
        reservationsView.adapter = ReservationAdapter(slotList, findNavController())
    }
}