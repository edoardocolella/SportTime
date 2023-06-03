package com.example.polito_mad_01.ui

import android.os.*
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.polito_mad_01.*
import com.example.polito_mad_01.adapters.FreeSlotAdapter
import com.example.polito_mad_01.model.Slot
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.*
import java.time.LocalDate


class Browse : Fragment(R.layout.fragment_browse) {

    lateinit var recyclerViewBrowse: RecyclerView
    var noFreeSlots : TextView? = null

    var slots : List<Slot> = listOf()

    private val vm: ShowFreeSlotsViewModel by viewModels{
        ShowFreeSlotsViewModelFactory((activity?.application as SportTimeApplication).reservationRepository)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {}.isEnabled = false



        noFreeSlots = UIUtils.findTextViewById(view, R.id.no_free_slots)
        noFreeSlots?.visibility = View.GONE
        recyclerViewBrowse =view.findViewById(R.id.recyclerViewBrowse)
        recyclerViewBrowse.layoutManager = LinearLayoutManager(view.context)

        val spinner = view.findViewById<Spinner>(R.id.spinnerBrowse)
        var selectedFilter = resources.getStringArray(R.array.sportArray)[0]

        val adapter = ArrayAdapter.createFromResource(
            view.context,
            R.array.sportArray,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner.adapter=adapter

        fun updateList(){
            val freeSlots = slots.filter { it.sport == selectedFilter }
            recyclerViewBrowse.adapter = FreeSlotAdapter(freeSlots, findNavController())

            if(freeSlots.isEmpty()){
                recyclerViewBrowse.visibility = View.GONE
                noFreeSlots?.visibility = View.VISIBLE
            }else{
                recyclerViewBrowse.visibility = View.VISIBLE
                noFreeSlots?.visibility = View.GONE
            }
        }

        vm.getFreeSlots(LocalDate.now().toString()).observe(viewLifecycleOwner) {
            slots = it
            updateList()
        }

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedFilter = resources.getStringArray(R.array.sportArray)[0]
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedFilter = spinner.selectedItem.toString()

                updateList()
            }
        }

    }
}
