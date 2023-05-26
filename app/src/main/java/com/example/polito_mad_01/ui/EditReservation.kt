package com.example.polito_mad_01.ui

import android.os.*
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.example.polito_mad_01.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.util.UIUtils.setTextView
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

            setSpinners(it)
            setImage(it.sport)
            setAllTextViews(it)
            setAllCheckBoxes(it)
            setButtonListener()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setSpinners(slot: Slot) {

        vm.getSlotsByPlayground(slot.playground_id).observe(viewLifecycleOwner) { list ->
            val dateTimeMap = sortedMapOf<String, List<String>>()

            val dateSpinner = view?.findViewById<Spinner>(R.id.dateSpinner)!!
            val timeSpinner = view?.findViewById<Spinner>(R.id.timeSpinner)!!

            list.forEach { s ->
                val date = s.date
                val times = dateTimeMap.getOrDefault(date, listOf())
                dateTimeMap[s.date] = times.plus("${s.start_time}-${s.end_time}")
            }

            val date = slot.date
            val times = dateTimeMap.getOrDefault(date, listOf())

            val reservationTime = "${slot.start_time}-${slot.end_time}"
            dateTimeMap[date] = times.plus(reservationTime)

            val dateAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                dateTimeMap.keys.toList()
            )

            dateSpinner.adapter = dateAdapter
            dateSpinner.setSelection(dateTimeMap.keys.toList().indexOf(date))
            dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    val chosenDate: String = dateTimeMap.keys.toList()[p2]
                    val timeAdapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_spinner_dropdown_item,
                        dateTimeMap[chosenDate]!!.toMutableList(),
                    )
                    timeSpinner.adapter = timeAdapter
                    val timeList = dateTimeMap[chosenDate]!!
                    timeSpinner.setSelection(timeList.indexOf(reservationTime))

                    //setNewTime(chosenDate, reservationTime)

                    timeSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                val chosenTime = timeList[p2]
                                setNewTime(chosenDate, chosenTime)
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }
                        }


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    disableSpinner(timeSpinner)
                }

            }

        }
    }

    private fun setNewTime(chosenDate: String, chosenTime: String) {
        val start = chosenTime.split("-")[0]
        val end = chosenTime.split("-")[1]
        vm.setActualTime(chosenDate, start, end)
    }

    private fun disableSpinner(timeSpinner: Spinner) {
        timeSpinner.isEnabled = false
        timeSpinner.isClickable = false
        timeSpinner.adapter = null
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


    private fun setAllTextViews(slot:Slot) {

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
            val actualStartTime = vm.reservation.value?.start_time!!
            val actualEndTime = vm.reservation.value?.end_time!!
            val actualDate = vm.reservation.value?.date!!
            val originalStartTime = vm.originalStartTime.value!!
            val originalEndTime = vm.originalEndTime.value!!
            val originalDate = vm.originalDate.value!!

            if (actualStartTime != originalStartTime || actualEndTime != originalEndTime || originalDate != actualDate) {
                //orario cambiato, necessario aggiornare sia lo slot precedentre che quello nuovo
                val oldReservation = vm.reservation.value?.copy()!!
                oldReservation.services.remove("heating")
                oldReservation.services.remove("equipment")
                oldReservation.services.remove("locker_room")
                oldReservation.services.remove("lighting")
                oldReservation.user_id = null
                oldReservation.start_time = originalStartTime
                oldReservation.end_time = originalEndTime
                oldReservation.date = originalDate

                vm.updateReservation(oldReservation)

                vm.getSlotByStartEndTimeDatePlayground(
                    actualStartTime,
                    actualEndTime,
                    actualDate,
                    oldReservation.playground_id
                ).observe(viewLifecycleOwner) {
                    val reservationWithIdRequested = it.copy()
                    val newSlot = vm.reservation.value?.copy()!!
                    newSlot.slot_id = reservationWithIdRequested.slot_id
                    vm.updateReservation(newSlot)
                    navigate(newSlot.slot_id)

                }

            } else {
                vm.updateReservation(vm.reservation.value?.copy()!!)
                navigate(vm.reservation.value?.slot_id!!)
            }

        } catch (e: Exception) {
            e.printStackTrace()
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
            R.id.action_editReservationFragment_to_showReservationFragment2,
            args
        )
    }

}