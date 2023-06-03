package com.example.polito_mad_01.ui

import android.os.*
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.polito_mad_01.R
import com.example.polito_mad_01.adapters.ReservationAdapter
import com.example.polito_mad_01.viewmodel.InvitationsViewModel

class ShowGames(val vm: InvitationsViewModel) : Fragment(R.layout.fragment_show_games) {
    private lateinit var reservationsView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.getReservations().observe(viewLifecycleOwner) {slotList ->
            reservationsView = view.findViewById(R.id.gameList)
            reservationsView.layoutManager = LinearLayoutManager(view.context)
            reservationsView.adapter = ReservationAdapter(slotList, findNavController())
        }
    }
}