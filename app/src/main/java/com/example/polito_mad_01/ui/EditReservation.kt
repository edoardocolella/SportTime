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
import com.example.polito_mad_01.db.*
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

            vm.setOriginalTime(it.slot.start_time, it.slot.end_time, it.slot.date)

            setSpinners(it)
            setImage(it.playground.sport_name)
            setAllTextViews(it)
            setAllCheckBoxes(it.slot)
            setButtonListener()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setSpinners(slotWithPlayground: SlotWithPlayground) {

        val slot = slotWithPlayground.slot
        val playground = slotWithPlayground.playground

        vm.getSlotsByPlayground(playground.playground_id).observe(viewLifecycleOwner) { list ->
            val dateTimeMap = sortedMapOf<String, List<String>>()

            val dateSpinner = view?.findViewById<Spinner>(R.id.dateSpinner)!!
            val timeSpinner = view?.findViewById<Spinner>(R.id.timeSpinner)!!

            list.forEach { sp ->
                val date = sp.slot.date
                val times = dateTimeMap.getOrDefault(date, listOf())
                dateTimeMap[sp.slot.date] = times.plus("${sp.slot.start_time}-${sp.slot.end_time}")
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

        println("chosenDate: $chosenDate")
        println("start: $start")
        println("end: $end")

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
            "Ping Pong" -> image.setImageResource(R.drawable.pingping_photo)
            else -> image.setImageResource(R.drawable.sport_photo)
        }
    }


    private fun setAllTextViews(slotWithPlayground: SlotWithPlayground) {
        val reservation = slotWithPlayground.slot
        val playground = slotWithPlayground.playground
        setTextView(R.id.playgroundName, playground.name)
        setTextView(R.id.playgroundLocation, playground.location)
        setTextView(R.id.playgroundSport, playground.sport_name)
        setTextView(R.id.reservationDate, reservation.date)
        setTextView(R.id.reservationTime, "${reservation.start_time}-${reservation.end_time}")
        setTextView(R.id.reservationTotalPrice, reservation.total_price.toString())
    }

    private fun setAllCheckBoxes(slot: Slot) {
        setCheckedBoxViewAndListener(R.id.reservationEquipment, slot.equipment, "equipment")
        setCheckedBoxViewAndListener(R.id.reservationHeating, slot.heating, "heating")
        setCheckedBoxViewAndListener(R.id.reservationLighting, slot.lighting, "lighting")
        setCheckedBoxViewAndListener(R.id.reservationLockerRoom, slot.locker_room, "locker_room")
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

    private fun trySaveData() {
        try {
            //vm.reservation.value?.slot?.let { vm.updateReservation() }

            val actualStartTime = vm.reservation.value?.slot?.start_time!!
            val actualEndTime = vm.reservation.value?.slot?.end_time!!
            val actualDate = vm.reservation.value?.slot?.date!!
            val originalStartTime = vm.originalStartTime.value!!
            val originalEndTime = vm.originalEndTime.value!!
            val originalDate = vm.originalDate.value!!

            println("actualStartTime: $actualStartTime")
            println("actualEndTime: $actualEndTime")
            println("originalStartTime: $originalStartTime")
            println("originalEndTime: $originalEndTime")

            if (actualStartTime != originalStartTime || actualEndTime != originalEndTime || originalDate != actualDate) {
                //orario cambiato, necessario aggiornare sia lo slot precedentre che quello nuovo
                val oldReservation = vm.reservation.value?.slot?.copy()!!
                oldReservation.heating = false
                oldReservation.equipment = false
                oldReservation.locker_room = false
                oldReservation.lighting = false
                oldReservation.user_id = null
                oldReservation.start_time = originalStartTime
                oldReservation.end_time = originalEndTime
                oldReservation.date = originalDate

                println("oldReservation: $oldReservation")
                vm.updateReservation(oldReservation)

                vm.getSlotByStartEndTimeDatePlayground(
                    actualStartTime,
                    actualEndTime,
                    actualDate,
                    oldReservation.playground_id
                ).observe(viewLifecycleOwner) {
                    val reservationWithIdRequested = it.copy()
                    val newSlot = vm.reservation.value?.slot?.copy()!!
                    newSlot.slot_id = reservationWithIdRequested.slot_id
                    slotID = reservationWithIdRequested.slot_id
                    println("newReservation: $newSlot")
                    vm.updateReservation(newSlot)

                }

            } else {
                vm.updateReservation(vm.reservation.value?.slot?.copy()!!)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_save_reservation) {
            trySaveData()
            val args = bundleOf("slotID" to slotID)

            findNavController().navigate(
                R.id.action_editReservationFragment_to_showReservationFragment2,
                args
            )
        }
        return true
    }

}