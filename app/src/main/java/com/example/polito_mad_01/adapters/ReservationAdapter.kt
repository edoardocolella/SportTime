package com.example.polito_mad_01.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.db.*

class ReservationAdapter(val data:List<SlotWithPlayground>) :
    RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>(){

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ReservationViewHolder {
        val v= LayoutInflater.from(parent.context)
            .inflate(R.layout.reservation_item_layout, parent, false)
        return ReservationViewHolder(v)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = data[position]
        holder.bind(reservation)
    }


    class ReservationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
/*        val name: TextView = v.findViewById(R.id.name)
        val address: TextView = v.findViewById(R.id.address)
        val icon: ImageView = v.findViewById(R.id.icon)*/

        fun bind(r: SlotWithPlayground) {
            val slot = r.slot
            val playground = r.playground



/*            name.text = u.text
            address.text = u.address
            icon.load(u.iconURL) //this requires library COIL*/
        }
    }
}