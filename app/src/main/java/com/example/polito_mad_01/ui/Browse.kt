package com.example.polito_mad_01.ui

import android.os.*
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.example.polito_mad_01.*
import com.example.polito_mad_01.db.SlotWithPlayground
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

            spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    selectedFilter = resources.getStringArray(R.array.sportArray)[0]
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedFilter = spinner.selectedItem.toString()

                    val freeSlots = slots.filter { it.playground.sport_name == selectedFilter }
                    println(freeSlots)
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
        private val playgroundName = findTextById(R.id.reservationPlayground, v)
        private val date = findTextById(R.id.reservationDate,v)
        private val time = findTextById(R.id.reservationTime, v)
        fun bind(fs: SlotWithPlayground){
            playgroundName.text = fs.playground.name
            date.text = fs.slot.date
            val startToEnd = "${fs.slot.start_time} - ${fs.slot.end_time}"
            time.text = startToEnd
        }

        private fun findTextById(id: Int, v: View): TextView {
            return v.findViewById<TextView>(id)
        }
    }


}