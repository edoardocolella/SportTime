package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.*
import com.example.polito_mad_01.viewmodel.*

class EditReservation : Fragment(R.layout.fragment_edit_reservation) {

    private var slotID = 0


    private val vm: EditReservationViewModel by viewModels {
        EditReservationViewModelFactory((activity?.application as SportTimeApplication).reservationRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.title  = "Editing"
        setAllView(view)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_reservation, menu)
    }

    private fun setAllView(view: View){
        slotID = requireArguments().getInt("slotID")
        vm.getReservation(slotID).observe(viewLifecycleOwner)
        {
            val reservation = it.slot
            val playground = it.playground

            val image : ImageView = view.findViewById(R.id.playgroundImage)
            when(it.playground.sport_name) {
                "Football" -> image.setImageResource(R.drawable.football_photo)
                "Basket" -> image.setImageResource(R.drawable.basketball_photo)
                "Volley" -> image.setImageResource(R.drawable.volleyball_photo)
                "Ping Pong" -> image.setImageResource(R.drawable.pingping_photo)
                else -> image.setImageResource(R.drawable.sport_photo)
            }

            setTextView(R.id.playgroundName, playground.name)
            setTextView(R.id.playgroundLocation, playground.location)
            setTextView(R.id.playgroundSport, playground.sport_name)
            setTextView(R.id.reservationDate, reservation.date)
            setTextView(R.id.reservationTime, "${reservation.start_time}-${reservation.end_time}")
            setTextView(R.id.reservationTotalPrice, reservation.total_price.toString())
            setCheckedBoxViewAndListener(R.id.reservationEquipment, reservation.equipment, "equipment")
            setCheckedBoxViewAndListener(R.id.reservationHeating, reservation.heating, "heating")
            setCheckedBoxViewAndListener(R.id.reservationLighting, reservation.lighting, "lighting")
            setCheckedBoxViewAndListener(R.id.reservationLockerRoom, reservation.locker_room, "locker_room")

            view?.findViewById<Button>(R.id.deleteButton)?.setOnClickListener {
                tryDeleteSlot()
                findNavController().navigate(R.id.action_editReservationFragment_to_showReservationFragment2)
            }

        }
    }

    private fun tryDeleteSlot() {
        try {
            vm.deleteReservation()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setTextView(viewId: Int, text: String) {
        view?.findViewById<TextView>(viewId)?.text = text
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

    private fun trySaveData(){
        try {
            vm.reservation.value?.slot?.let { vm.updateReservation() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val args = bundleOf(
            "slotID" to slotID
        )
        if (item.itemId == R.id.action_save_reservation)
            trySaveData()
        findNavController().navigate(
            R.id.action_editReservationFragment_to_showReservationFragment2,
            args
        )
        return true
    }

}