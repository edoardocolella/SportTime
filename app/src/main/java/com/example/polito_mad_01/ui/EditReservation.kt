package com.example.polito_mad_01.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
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
            val reservation = it.slot
            val playground = it.playground

            /*
            setTextView(R.id.playgroundName, playground.name)
            setTextView(R.id.playgroundDescription, playground.description)
            setTextView(R.id.playgroundLocation, playground.location)
            setTextView(R.id.playgroundSport, playground.sport_name)
            setTextView(R.id.reservationDate, reservation.date)
            setTextView(R.id.reservationStartTime, reservation.start_time)
            setTextView(R.id.reservationEndTime, reservation.end_time)
            setTextView(R.id.reservationTotalPrice, reservation.total_price.toString())
            setCheckedBoxViewAndListener(R.id.reservationEquipment, reservation.equipment, "equipment")
            setCheckedBoxViewAndListener(R.id.reservationHeating, reservation.heating, "heating")
            setCheckedBoxViewAndListener(R.id.reservationLighting, reservation.lighting, "lighting")
            setCheckedBoxViewAndListener(R.id.reservationLockerRoom, reservation.locker_room, "locker_room")
            */
        }
    }

    private fun setTextView(viewId: Int, text: String) {
        view?.findViewById<EditText>(viewId)?.setText(text)
    }
    private fun setCheckedBoxViewAndListener(id: Int, availability: Boolean, attribute: String) {
        val checkBox = view?.findViewById<CheckBox>(id)
        checkBox?.isChecked = availability
        checkBox?.setOnCheckedChangeListener { _, isChecked ->
            setExtra(attribute, isChecked)
        }
    }

    private fun setExtra(attribute: String, checked: Boolean) {
        when (attribute) {
            "heating" -> vm.reservation.value?.slot?.heating = checked
            "equipment" -> vm.reservation.value?.slot?.equipment = checked
            "locker_room" -> vm.reservation.value?.slot?.locker_room = checked
            "lighting" -> vm.reservation.value?.slot?.lighting = checked
        }
    }

    fun trySaveData(){
        try {
            vm.reservation.value?.slot?.let { vm.updateReservation(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}