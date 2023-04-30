package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.db.ReservationDao
import com.example.polito_mad_01.db.SlotWithPlayground
import com.example.polito_mad_01.viewmodel.ReservationsViewModel
import com.example.polito_mad_01.viewmodel.ReservationsViewModelFactory
import com.example.polito_mad_01.viewmodel.ShowReservationsViewModel
import com.example.polito_mad_01.viewmodel.ShowReservationsViewModelFactory

class ShowReservation : Fragment(R.layout.fragment_show_reservation) {
    private lateinit var slotID : String

    private val vm: ShowReservationsViewModel by viewModels {
        ShowReservationsViewModelFactory((activity?.application as SportTimeApplication).showReservationsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slotID = requireArguments().getInt("slotID").toString()

        vm.getReservation(slotID.toInt()).observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.playgroundName).text = it.playground.name

            view.findViewById<TextView>(R.id.playgroundLocation).text = it.playground.location
            view.findViewById<TextView>(R.id.playgroundSport).text = it.playground.sport_name
            view.findViewById<TextView>(R.id.playgroundPrice).text = it.playground.price_per_slot.toString() + "€"

            view.findViewById<TextView>(R.id.slotDate).text = it.slot.date
            view.findViewById<TextView>(R.id.slotTime).text = it.slot.start_time + " - " + it.slot.end_time
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_show_reservation, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val args = bundleOf(
            "slotID" to slotID
        )

        if (item.itemId == R.id.action_edit_reservation)
            findNavController().navigate(R.id.action_showReservationFragment2_to_editReservationFragment, args)
        return true
    }
}