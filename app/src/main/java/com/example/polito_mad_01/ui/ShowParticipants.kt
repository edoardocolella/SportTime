package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.viewmodel.ShowReservationsViewModel

class ShowParticipants(val slotID: Int, val vm: ShowReservationsViewModel) : Fragment(R.layout.fragment_show_participants) {

    private lateinit var recyclerViewPartecipants: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println(vm.getReservationParticipants(slotID))

        /*recyclerViewPartecipants =view.findViewById(R.id.partecipantsRecyclerView)
        recyclerViewPartecipants.layoutManager = LinearLayoutManager(view.context)*/
    }
}