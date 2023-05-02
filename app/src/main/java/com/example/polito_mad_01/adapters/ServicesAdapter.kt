package com.example.polito_mad_01.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R

class ServicesAdapter(private val data : List<String>) : RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder>(){
    class ServicesViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        fun bind(service: String){
            view.findViewById<TextView>(R.id.serviceItem).text = service
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val v= LayoutInflater.from(parent.context)
            .inflate(R.layout.service_item_layout, parent, false)
        return ServicesAdapter.ServicesViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        val service = data[position]
        holder.bind(service)
    }

}