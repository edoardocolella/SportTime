package com.example.polito_mad_01.ui

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.*
import com.example.polito_mad_01.viewmodel.*
import java.util.SortedMap

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
        setAllView(view)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_reservation, menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAllView(view: View){
        slotID = requireArguments().getInt("slotID")

        vm.getReservation(slotID).observe(viewLifecycleOwner)
        {
            val reservation = it.slot
            val playground = it.playground

            vm.getSlotsByPlayground(playground.playground_id).observe(viewLifecycleOwner) { list->
                val dateTimeMap : SortedMap<String, List<String>> = sortedMapOf()

                val dateSpinner = view.findViewById<Spinner>(R.id.dateSpinner)
                val timeSpinner = view.findViewById<Spinner>(R.id.timeSpinner)

                list.forEach { sp ->
                    val date = sp.slot.date
                    val times = dateTimeMap.getOrDefault(date, listOf())

                    dateTimeMap[sp.slot.date] = times.plus("${sp.slot.start_time}-${sp.slot.end_time}")
                }
                val date = it.slot.date
                val times = dateTimeMap.getOrDefault(date, listOf())

                val reservationTime = "${it.slot.start_time}-${it.slot.end_time}"
                dateTimeMap[date] = times.plus(reservationTime)

                val dateAdapter = ArrayAdapter(
                    view.context,
                    android.R.layout.simple_spinner_dropdown_item,
                    dateTimeMap.keys.toList()
                )
                dateSpinner.adapter = dateAdapter
                dateSpinner.setSelection(dateTimeMap.keys.toList().indexOf(date))

                dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        val date : String = dateTimeMap.keys.toList()[p2]

                        val timeAdapter = ArrayAdapter(
                            view.context,
                            android.R.layout.simple_spinner_dropdown_item,
                            dateTimeMap[date]!!.toMutableList(),
                        )
                        timeSpinner.adapter = timeAdapter

                        val timeList = dateTimeMap[date]!!
                        timeSpinner.setSelection(timeList.indexOf(reservationTime))
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        timeSpinner.isEnabled = false
                        timeSpinner.isClickable = false
                        timeSpinner.adapter = null
                    }

                }

            }

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
                showDeleteDialog()
            }

        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Reservation")
            .setMessage("Are you sure you want to delete this reservation?")
            .setPositiveButton("Yes") { _, _ ->
                tryDeleteSlot()
                findNavController().navigate(R.id.action_editReservationFragment_to_reservationsFragment)
                Toast.makeText(requireContext(), "Reservation deleted", Toast.LENGTH_SHORT).show()
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