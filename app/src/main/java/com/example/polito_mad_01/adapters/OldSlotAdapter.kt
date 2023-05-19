package com.example.polito_mad_01.adapters

import android.view.*
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.Slot


class OldSlotAdapter(
    private val data:List<Slot>,
    private val navController: NavController
): RecyclerView.Adapter<OldSlotAdapter.OldSlotHolder>(){
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OldSlotHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.old_slot_item_layout ,parent, false)
        return OldSlotHolder(v, navController)
    }

    override fun onBindViewHolder(holder: OldSlotHolder, position: Int) {
        val fs = data[position]
        holder.bind(fs)
    }

    class OldSlotHolder(v: View, private val navController: NavController): RecyclerView.ViewHolder(v){
        private val playgroundName = findTextById(R.id.oldResPlaygroundName, v)
        private val date = findTextById(R.id.oldResSlotDate,v)
        private val time = findTextById(R.id.oldResSlotTime, v)
        private val sportImage : ImageView = v.findViewById(R.id.oldResSportImage)
        //private val price = findTextById(R.id.oldResPlaygroundPrice, v)

        private val oldSlotItem: View = v.findViewById(R.id.internalLayout)

        fun bind(fs: Slot){
            //price.text = fs.playground.price_per_slot.toString()
            playgroundName.text = fs.playground.name
            date.text = fs.date
            val startToEnd = "${fs.start_time} - ${fs.end_time}"
            time.text = startToEnd

            oldSlotItem.setOnClickListener{
                navController.navigate(
                    R.id.action_showOldReservations_to_showOldReservation, bundleOf(
                        "slotId" to fs.slot_id,
                        "userId" to fs.user_id,
                        "playgroundId" to fs.playground_id
                    )
                )
            }

            sportImage.setImageResource(when(fs.playground.sport_name){
                "Football" -> R.drawable.sports_soccer_48px
                "Volley" -> R.drawable.sports_volleyball_48px
                "Ping Pong" -> R.drawable.sports_tennis_48px
                "Basket" -> R.drawable.sports_basketball_48px
                else -> R.drawable.selection_background
            })
        }

        private fun findTextById(id: Int, v: View): TextView {
            return v.findViewById(id)
        }
    }


}