package com.example.polito_mad_01.ui

import android.os.*
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.example.polito_mad_01.*
import com.example.polito_mad_01.adapters.FreeSlotAdapter
import com.example.polito_mad_01.viewmodel.*
import java.time.LocalDate

class Browse : Fragment(R.layout.fragment_browse) {

    lateinit var recyclerViewBrowse: RecyclerView
    lateinit var noFreeSlots : TextView

    private val vm: ShowFreeSlotsViewModel by viewModels{
        ShowFreeSlotsViewModelFactory((activity?.application as SportTimeApplication).reservationRepository)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noFreeSlots = view.findViewById(R.id.no_free_slots)
        noFreeSlots.visibility = View.GONE
        recyclerViewBrowse =view.findViewById(R.id.recyclerViewBrowse)
        recyclerViewBrowse.layoutManager = LinearLayoutManager(view.context)

        activity?.actionBar?.title = "Browse"

        val spinner = view.findViewById<Spinner>(R.id.spinnerBrowse)
        var selectedFilter: String

        val adapter = ArrayAdapter.createFromResource(
            view.context,
            R.array.sportArray,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner.adapter=adapter

        vm.getFreeSlots(LocalDate.now().toString()).observe(viewLifecycleOwner) { slots ->
            println(LocalDate.now().toString())

            spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    selectedFilter = resources.getStringArray(R.array.sportArray)[0]
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedFilter = spinner.selectedItem.toString()
                    val freeSlots = slots.filter { it.playground.sport_name == selectedFilter }
                    recyclerViewBrowse.adapter = FreeSlotAdapter(freeSlots)

                    if(freeSlots.isEmpty()){
                        recyclerViewBrowse.visibility = View.GONE
                        noFreeSlots.visibility = View.VISIBLE
                    }else{
                        recyclerViewBrowse.visibility = View.VISIBLE
                        noFreeSlots.visibility = View.GONE
                    }
                }
            }
        }

    }
}
