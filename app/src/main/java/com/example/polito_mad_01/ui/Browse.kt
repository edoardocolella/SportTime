package com.example.polito_mad_01.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.db.ReservationDao
import com.example.polito_mad_01.db.SlotWithPlayground
import com.example.polito_mad_01.repositories.ReservationRepository
import com.example.polito_mad_01.viewmodel.ShowFreeSlotsViewModel
import com.example.polito_mad_01.viewmodel.ShowFreeSlotsViewModelFactory
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import java.text.DateFormat
import java.time.LocalDate

class Browse : Fragment(R.layout.fragment_browse) {

    private val vm: ShowFreeSlotsViewModel by viewModels{
        ShowFreeSlotsViewModelFactory((activity?.application as SportTimeApplication).reservationRepository)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.title = "Browse"

        val spinner = view.findViewById<Spinner>(R.id.spinnerBrowse)
        val items = listOf<String>("Tennis", "Volleyball", "Basketball", "Football")
        var selectedFilter = resources.getStringArray(R.array.sportArray)[0]
        val freeSlotsLiveData: LiveData<List<SlotWithPlayground>>
        lateinit var freeSlots: List<SlotWithPlayground>

        val adapter = ArrayAdapter.createFromResource(
            view.context,
            R.array.sportArray,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner.adapter=adapter

        //vm.getFreeSlots(LocalDate.now().toString()).observe(viewLifecycleOwner){
        vm.getFreeSlots("01-01-2019").observe(viewLifecycleOwner){
            freeSlots = it
            println(it)
        }

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedFilter = resources.getStringArray(R.array.sportArray)[0]
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedFilter = spinner.selectedItem.toString()
                val recyclerViewBrowse = view.findViewById<RecyclerView>(R.id.recyclerViewBrowse)
                recyclerViewBrowse.adapter=FreeSlotAdapter(freeSlots.filter { it.playground.sport_name == selectedFilter })
            }
        }


    }
}

class FreeSlotAdapter(val data:List<SlotWithPlayground>): RecyclerView.Adapter<FreeSlotAdapter.FreeSlotHolder>(){
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreeSlotHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.reservation_item_layout ,parent, false)
        return FreeSlotHolder(v)
    }

    override fun onBindViewHolder(holder: FreeSlotHolder, position: Int) {
        val fs = data[position]
        holder.bind(fs)
    }

    class FreeSlotHolder(v: View): RecyclerView.ViewHolder(v){
        val playgroundName = v.findViewById<TextView>(R.id.res_playground_name)
        val date = v.findViewById<TextView>(R.id.res_date)
        val time = v.findViewById<TextView>(R.id.res_time)
        fun bind(fs: SlotWithPlayground){
            playgroundName.text = fs.playground.name
            date.text = fs.slot.date
            time.text = fs.slot.date
        }
    }

}