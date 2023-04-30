package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.*
import com.example.polito_mad_01.viewmodel.*

class ShowReservation : Fragment(R.layout.fragment_show_reservation) {
    private var slotID = 0

    private val vm: ShowReservationsViewModel by viewModels {
        ShowReservationsViewModelFactory((activity?.application as SportTimeApplication).showReservationsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slotID = requireArguments().getInt("slotID")
        vm.getReservation(slotID).observe(viewLifecycleOwner) {
            setTextView(R.id.playgroundName, it.playground.name)
            setTextView(R.id.playgroundLocation, it.playground.location)
            setTextView(R.id.playgroundSport, it.playground.sport_name)
            val stringPrice = it.playground.price_per_slot.toString() + "€"
            setTextView(R.id.playgroundPrice, stringPrice)
            setTextView(R.id.slotDate, it.slot.date)
            val stringTime = "${it.slot.start_time} - ${it.slot.end_time}"
            setTextView(R.id.slotTime, stringTime)
            setCheckedBoxView(R.id.reservationEquipment, it.slot.equipment)
            setCheckedBoxView(R.id.reservationHeating, it.slot.heating)
            setCheckedBoxView(R.id.reservationLighting, it.slot.lighting)
            setCheckedBoxView(R.id.reservationLockerRoom, it.slot.locker_room)
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
            findNavController().navigate(
                R.id.action_showReservationFragment2_to_editReservationFragment,
                args
            )
        return true
    }

    private fun setTextView(viewId: Int, text: String) {
        view?.findViewById<TextView>(viewId)?.text = text
    }

    private fun setCheckedBoxView(id: Int, availability: Boolean) {
        val checkBox = view?.findViewById<CheckBox>(id)
        checkBox?.isChecked = availability
    }
}