package com.example.polito_mad_01.ui

import android.os.*
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.model.Slot
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.*
import java.time.*

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
            setImage(it.playground_id)
            setAllTextViews(it)
            setAllCheckBoxes(it)
            setAllButtons()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAllButtons() {

        val button = view?.findViewById<Button>(R.id.createReservation)
        val res = vm.reservation.value!!
        if(res.date >= LocalDate.now().toString() && res.start_time > LocalTime.now().toString()) {
            button?.setOnClickListener {
                vm.createReservation()
                findNavController().navigate(R.id.reservationsFragment)
            }
        }
        else{
            button?.text = "You cannot reserve a slot in the past"
            button?.isClickable = false
            button?.isEnabled = false
        }
    }

    private fun setImage(playground_id: Int) {
        val image: ImageView = requireView().findViewById(R.id.playgroundImage)
        vm.getPlaygroundImage(playground_id).observe(viewLifecycleOwner)
        {imageUri -> imageUri?.let { image.setImageURI(it) } }
    }


    private fun setAllTextViews(slot: Slot) {

        UIUtils.setTextView(R.id.playgroundName, slot.playgroundName, view)
        UIUtils.setTextView(R.id.playgroundLocation, slot.location, view)
        UIUtils.setTextView(R.id.playgroundSport, slot.sport, view)
        UIUtils.setTextView(R.id.dateText, slot.date, view)
        UIUtils.setTextView(R.id.timeText, "${slot.start_time}-${slot.end_time}", view)
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