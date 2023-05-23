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
            requireActivity().onBackPressedDispatcher
                .addCallback(this) {
                    val args = bundleOf(
                        "selectedDate" to it.date
                    )
                    findNavController().navigate(R.id.action_showReservationFragment2_to_reservationsFragment, args)
                }
                .isEnabled = true

            setTextView(R.id.playgroundName, it.playground.name, view)
            setTextView(R.id.playgroundLocation, it.playground.location, view)
            setTextView(R.id.playgroundSport, it.playground.sport_name, view)

            val image : ImageView = view.findViewById(R.id.playgroundImage)
            when(it.playground.sport_name) {
                "Football" -> image.setImageResource(R.drawable.football_photo)
                "Basket" -> image.setImageResource(R.drawable.basketball_photo)
                "Volley" -> image.setImageResource(R.drawable.volleyball_photo)
                "Ping Pong" -> image.setImageResource(R.drawable.pingpong_photo)
                else -> image.setImageResource(R.drawable.sport_photo)
            }

            val stringPrice = it.playground.price_per_slot.toString() + "€"
            setTextView(R.id.playgroundPrice, stringPrice, view)
            setTextView(R.id.slotDate, it.date, view)
            val stringTime = "${it.start_time}-${it.end_time}"
            setTextView(R.id.slotTime, stringTime, view)

            val services = mutableListOf<String>()
            if(it.services.containsKey("equipment")) services.add("- Equipment")
            if(it.services.containsKey("heating")) services.add("- Heating")
            if(it.services.containsKey("lighting")) services.add("- Lightning")
            if(it.services.containsKey("locker_room")) services.add("- Locker room")

            view.findViewById<RecyclerView>(R.id.servicesView).let{list ->
                list.layoutManager = LinearLayoutManager(view.context)
                list.adapter = ServicesAdapter(services)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_show_reservation, menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val args = bundleOf(
            "slotID" to slotID
        )

        if (item.itemId == R.id.action_edit_reservation){
            if(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) >
                vm.slot.value?.date!! ) {
                Toast.makeText(requireContext(), "You cannot edit past reservations", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(
                    R.id.action_showReservationFragment2_to_editReservationFragment,
                    args
                )
            }
        }
        return true
    }

}