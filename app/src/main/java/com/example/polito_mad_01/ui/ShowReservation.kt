package com.example.polito_mad_01.ui

import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.*
import com.example.polito_mad_01.adapters.ServicesAdapter
import com.example.polito_mad_01.util.UIUtils.setTextView
import com.example.polito_mad_01.viewmodel.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ShowReservation : Fragment(R.layout.fragment_show_reservation) {
    private var slotID = 0

    private val vm: ShowReservationsViewModel by viewModels {
        ShowReservationsViewModelFactory((activity?.application as SportTimeApplication).reservationRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {}.isEnabled = false

        slotID = requireArguments().getInt("slotID")

        vm.getReservation(slotID).observe(viewLifecycleOwner) {
            requireActivity().onBackPressedDispatcher
                .addCallback(this) {
                    val args = bundleOf(
                        "selectedDate" to it.date
                    )
                    findNavController().navigate(R.id.action_showReservationFragment2_to_reservationsFragment, args)
                }
                .isEnabled = true

            setTextView(R.id.playgroundName, it.playgroundName, view)
            setTextView(R.id.playgroundLocation, it.location, view)
            setTextView(R.id.playgroundSport, it.sport, view)

            val image : ImageView = view.findViewById(R.id.playgroundImage)
            vm.getPlaygroundImage(it.playground_id).observe(viewLifecycleOwner)
            {imageUri -> imageUri?.let { image.setImageURI(imageUri) } }

            val stringPrice = it.total_price.toString() + "€"
            setTextView(R.id.playgroundPrice, stringPrice, view)
            setTextView(R.id.slotDate, it.date, view)
            val stringTime = "${it.start_time}-${it.end_time}"
            setTextView(R.id.slotTime, stringTime, view)

            val services = mutableListOf<String>()
            if(it.services.getOrDefault("equipment",false)) services.add("- Equipment")
            if(it.services.getOrDefault("heating",false)) services.add("- Heating")
            if(it.services.getOrDefault("lighting",false)) services.add("- Lightning")
            if(it.services.getOrDefault("locker_room",false)) services.add("- Locker room")

            view.findViewById<RecyclerView>(R.id.servicesView).let{list ->
                list.layoutManager = LinearLayoutManager(view.context)
                list.adapter = ServicesAdapter(services)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        vm.slot.observe(viewLifecycleOwner) {
            if (LocalDate.now().toString() < it.date) {
                inflater.inflate(R.menu.menu_show_reservation, menu)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val args = bundleOf(
            "slotID" to slotID
        )

        if (item.itemId == R.id.action_edit_reservation){
                findNavController().navigate(R.id.editReservationFragment, args)
        }
        return true
    }

}