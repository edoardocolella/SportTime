package com.example.polito_mad_01.ui

import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.util.UIUtils.setTextView
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.snackbar.Snackbar

class EditReservation : Fragment(R.layout.fragment_edit_reservation) {

    private var slotID = 0
    private val vm: EditReservationViewModel by viewModels {
        EditReservationViewModelFactory((activity?.application as SportTimeApplication).reservationRepository)
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

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_reservation, menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAllView() {

        slotID = requireArguments().getInt("slotID")
        vm.getReservation(slotID).observe(viewLifecycleOwner)
        {

            vm.setOriginalTime(it.start_time, it.end_time, it.date)

            setImage(it.playground_id)
            setAllTextViews(it)
            setAllCheckBoxes(it)
            setButtonListener()

        }
    }

    private fun setImage(playground_id: Int) {
        val image: ImageView = requireView().findViewById(R.id.playgroundImage)
        vm.getPlaygroundImage(playground_id).observe(viewLifecycleOwner)
        {imageUri -> imageUri?.let { image.setImageURI(it) } }
    }


    private fun setAllTextViews(slot:Slot) {

        setTextView(R.id.time, "${slot.start_time}-${slot.end_time}",view)
        setTextView(R.id.date, slot.date,view)
        setTextView(R.id.playgroundName, slot.playgroundName,view)
        setTextView(R.id.playgroundLocation, slot.location,view)
        setTextView(R.id.playgroundSport, slot.sport,view)
        setTextView(R.id.reservationDate, slot.date,view)
        setTextView(R.id.reservationTime, "${slot.start_time}-${slot.end_time}",view)
        setTextView(R.id.reservationTotalPrice, slot.total_price.toString(),view)
    }

    private fun setAllCheckBoxes(slot: Slot) {
        setCheckedBoxViewAndListener(R.id.reservationEquipment, slot.services.getOrDefault("equipment",false), "equipment")
        setCheckedBoxViewAndListener(R.id.reservationHeating, slot.services.getOrDefault("heating", false), "heating")
        setCheckedBoxViewAndListener(R.id.reservationLighting, slot.services.getOrDefault("lighting",false), "lighting")
        setCheckedBoxViewAndListener(R.id.reservationLockerRoom, slot.services.getOrDefault("locker_room",false), "locker_room")
    }

    private fun setButtonListener() {
        view?.findViewById<Button>(R.id.deleteButton)?.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Reservation")
            .setMessage("Are you sure you want to delete this reservation?")
            .setPositiveButton("Yes") { _, _ ->
                tryDeleteSlot()
                findNavController().navigate(R.id.action_editReservationFragment_to_reservationsFragment)
                Snackbar.make(requireView(), "Reservation deleted", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("No") { _, _ -> }
            .create().show()
    }

    private fun tryDeleteSlot() {
        try {
            vm.deleteReservation()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    private fun trySaveData(){
        try {
            vm.updateReservation()
            navigate(vm.reservation.value?.slot_id!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Snackbar.make(requireView(), "Error while saving data", Snackbar.LENGTH_LONG).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_save_reservation) {
            trySaveData()
        }
        return true
    }

    private fun navigate(newSlotID: Int){
        val args = bundleOf("slotID" to newSlotID)
        findNavController().navigate(
            R.id.reservationContainer,
            args
        )
    }

}