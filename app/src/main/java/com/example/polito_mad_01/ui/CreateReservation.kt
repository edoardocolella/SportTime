package com.example.polito_mad_01.ui

import android.os.*
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.model.Slot
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.*

class CreateReservation : Fragment(R.layout.fragment_create_reservation) {

    private var slotID = 0
    private val vm: CreateReservationViewModel by viewModels {
        CreateReservationViewModelFactory((activity?.application as SportTimeApplication).reservationRepository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAllView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAllView() {

        slotID = requireArguments().getInt("slotID")
        vm.getReservation(slotID).observe(viewLifecycleOwner)
        {

            //vm.setOriginalTime(it.start_time, it.end_time, it.date)
            //setSpinners(it)
            setImage(it.sport)
            setAllTextViews(it)
            setAllCheckBoxes(it)
        }
    }

    private fun setImage(sportName: String) {
        val image: ImageView = requireView().findViewById(R.id.playgroundImage)
        when (sportName) {
            "Football" -> image.setImageResource(R.drawable.football_photo)
            "Basket" -> image.setImageResource(R.drawable.basketball_photo)
            "Volley" -> image.setImageResource(R.drawable.volleyball_photo)
            "Ping Pong" -> image.setImageResource(R.drawable.pingpong_photo)
            else -> image.setImageResource(R.drawable.sport_photo)
        }
    }

    private fun setAllTextViews(slot: Slot) {

        UIUtils.setTextView(R.id.playgroundName, slot.playgroundName, view)
        UIUtils.setTextView(R.id.playgroundLocation, slot.location, view)
        UIUtils.setTextView(R.id.playgroundSport, slot.sport, view)
        UIUtils.setTextView(R.id.reservationDate, slot.date, view)
        UIUtils.setTextView(R.id.reservationTime, "${slot.start_time}-${slot.end_time}", view)
        UIUtils.setTextView(R.id.reservationTotalPrice, slot.total_price.toString(), view)
    }

    private fun setAllCheckBoxes(slot: Slot) {
        setCheckedBoxViewAndListener(R.id.reservationEquipment, slot.services.getOrDefault("equipment",false), "equipment")
        setCheckedBoxViewAndListener(R.id.reservationHeating, slot.services.getOrDefault("heating", false), "heating")
        setCheckedBoxViewAndListener(R.id.reservationLighting, slot.services.getOrDefault("lighting",false), "lighting")
        setCheckedBoxViewAndListener(R.id.reservationLockerRoom, slot.services.getOrDefault("locker_room",false), "locker_room")
    }

    private fun setCheckedBoxViewAndListener(id: Int, availability: Boolean, attribute: String) {
        val checkBox = view?.findViewById<CheckBox>(id)
        checkBox?.isChecked = availability
        checkBox?.setOnCheckedChangeListener { _, isChecked ->
            setExtra(attribute, isChecked)
        }
    }

    private fun setExtra(attribute: String, checked: Boolean) {
        vm.reservation.value?.services?.put(attribute, checked)
    }

}