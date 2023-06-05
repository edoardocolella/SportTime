package com.example.polito_mad_01.adapters

import android.view.*
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.*
import com.google.firebase.auth.FirebaseAuth

class ReservationAdapter(
    private val data: List<Slot>,
    private val navController: NavController
) :
    RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.reservation_item_layout, parent, false)
        return ReservationViewHolder(v, navController)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = data[position]
        holder.bind(reservation)
    }


    class ReservationViewHolder(view: View, private val navController: NavController) :
        RecyclerView.ViewHolder(view) {
        private val playgroundName = findTextViewById(R.id.reservationPlayground)
        private val slotDate = findTextViewById(R.id.reservationDate)
        private val slotTime = findTextViewById(R.id.reservationTime)
        private val sportImage = view.findViewById<ImageView>(R.id.reservationSportImage)
        private val slotOwnerIcon = view.findViewById<ImageView>(R.id.slotOwnerIcon)

        private val reservationItem: View = view.findViewById(R.id.reservationItem)


        private fun findTextViewById(id: Int): TextView {
            return itemView.findViewById(id)
        }

        fun bind(slot: Slot) {
            reservationItem.setOnClickListener {
                when(navController.currentDestination?.id){
                    R.id.reservationsFragment -> {
                        navController.navigate(
                            R.id.action_reservationsFragment_to_reservationContainer, bundleOf("slotID" to slot.slot_id)
                        )
                    }
                    R.id.invitationContainer -> {
                        navController.navigate(
                            R.id.action_invitationContainer_to_reservationContainer, bundleOf("slotID" to slot.slot_id)
                        )
                    }
                }

            }

            playgroundName.text = slot.playgroundName
            slotDate.text = slot.date
            val timeString = "${slot.start_time}-${slot.end_time}"
            slotTime.text = timeString
            sportImage.setImageResource(
                when (slot.sport) {
                    "Football" -> R.drawable.sports_soccer_48px
                    "Volley" -> R.drawable.sports_volleyball_48px
                    "Ping Pong" -> R.drawable.sports_tennis_48px
                    "Basket" -> R.drawable.sports_basketball_48px
                    else -> R.drawable.selection_background
                }
            )

            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            if(slot.user_id == userId){
                slotOwnerIcon.visibility = View.VISIBLE
            } else {
                slotOwnerIcon.visibility = View.GONE
            }
        }
    }
}