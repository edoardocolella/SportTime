package com.example.polito_mad_01.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.db.*

class ReservationAdapter(val data:List<SlotWithPlayground>, val navController: NavController) :
    RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>(){

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ReservationViewHolder {
        val v= LayoutInflater.from(parent.context)
            .inflate(R.layout.reservation_item_layout, parent, false)
        return ReservationViewHolder(v, navController)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = data[position]
        holder.bind(reservation)
    }


    class ReservationViewHolder(view: View, val navController: NavController) : RecyclerView.ViewHolder(view) {
        val playgroundName : TextView = view.findViewById(R.id.reservationPlayground)
        val reservationItem : View = view.findViewById(R.id.reservationItem)

        fun bind(r: SlotWithPlayground) {

            reservationItem.setOnClickListener {
                val b : Bundle = bundleOf(
                    "reservationID" to r.slot.slot_id
                )
                b.putString("test", "test")
                navController.navigate(R.id.action_reservationsFragment_to_showReservationFragment2, b)
            }

            val slot = r.slot
            val playground = r.playground

            playgroundName.text = playground.name


/*            name.text = u.text
            address.text = u.address
            icon.load(u.iconURL) //this requires library COIL*/
        }
    }
}