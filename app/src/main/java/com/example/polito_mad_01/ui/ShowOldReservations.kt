package com.example.polito_mad_01.ui

import android.os.*
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.adapters.OldSlotAdapter
import com.example.polito_mad_01.viewmodel.*
import java.time.LocalDate

class ShowOldReservations : Fragment(R.layout.fragment_show_old_reservations) {
    private lateinit var recyclerViewOldRes: RecyclerView
    private lateinit var noOldSlots: TextView

    private val vm: ShowOldReservationsViewModel by viewModels{
        ShowOldReservationsViewModelFactory((activity?.application as SportTimeApplication).reservationRepository)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {}.isEnabled = false


        noOldSlots = view.findViewById(R.id.no_old_res_tv)
        noOldSlots.visibility=View.GONE
        recyclerViewOldRes =view.findViewById(R.id.oldResRecyclerView)
        recyclerViewOldRes.layoutManager = LinearLayoutManager(view.context)

        vm.getOldReservations(LocalDate.now().toString()).observe(viewLifecycleOwner){ oldSlots ->
            recyclerViewOldRes.adapter= OldSlotAdapter(oldSlots, findNavController())
            if(oldSlots.isEmpty()){
                recyclerViewOldRes.visibility=View.GONE
                noOldSlots.visibility=View.VISIBLE
            }else{
                recyclerViewOldRes.visibility=View.VISIBLE
                noOldSlots.visibility=View.GONE
            }
        }
    }

}