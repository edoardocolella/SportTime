package com.example.polito_mad_01.ui

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
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.db.Slot

class Browse : Fragment(R.layout.fragment_browse) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.title = "Browse"

        val spinner = view.findViewById<Spinner>(R.id.spinnerBrowse)
        val items = listOf<String>("Tennis", "Volleyball", "Basketball", "Football")
        var selectedFilter = resources.getStringArray(R.array.sportArray)[0]

        val adapter = ArrayAdapter.createFromResource(
            view.context,
            R.array.sportArray,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner.adapter=adapter


        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedFilter = resources.getStringArray(R.array.sportArray)[0]
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedFilter = spinner.selectedItem.toString()
            }
        }

        val recyclerViewBrowse = view.findViewById<RecyclerView>(R.id.recyclerViewBrowse)
        recyclerViewBrowse.adapter=FreeSlotAdapter()
    }
}

class FreeSlotAdapter(val data:List<Slot>): RecyclerView.Adapter<FreeSlotAdapter.FreeSlotHolder>(){
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreeSlotHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.reservation_item_layout ,parent, false)
        return FreeSlotHolder(v)
    }

    override fun onBindViewHolder(holder: FreeSlotHolder, position: Int) {
        val fs = data[position]
        holder.bind(fs)
    }

    class FreeSlotHolder(v:View): RecyclerView.ViewHolder(v){
        fun bind(fs: Slot){

        }
    }

}