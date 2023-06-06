package com.example.polito_mad_01.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.Invitation
import com.example.polito_mad_01.model.InvitationInfo
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.InvitationsViewModel

class InvitationAdapter(private val data: List<InvitationInfo>,
                        private val vm: InvitationsViewModel,
                        private val owner: LifecycleOwner,
                        private val navController: NavController,): RecyclerView.Adapter<InvitationHolder>(){

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.invitation_item_layout ,parent, false)
        return InvitationHolder(v, vm, navController)
    }

    override fun onBindViewHolder(holder: InvitationHolder, position: Int) {
        val invitation = data[position]
        holder.bind(invitation, owner)
    }
}

class InvitationHolder(private val v: View,
                       private val vm: InvitationsViewModel,
                       private val navController: NavController): RecyclerView.ViewHolder(v){

    private val sender = UIUtils.findTextViewById(v, R.id.senderTextView)!!
    private val playgroundName = UIUtils.findTextViewById(v, R.id.reservationPlayground)!!
    private val date = UIUtils.findTextViewById(v, R.id.reservationDate)!!
    private val time = UIUtils.findTextViewById(v, R.id.reservationTime)!!

    private val sportImage : ImageView = v.findViewById(R.id.reservationSportImage)
    private val invitationItem: View = v.findViewById(R.id.invitationItem)

    fun bind(invitationInfo: InvitationInfo, owner: LifecycleOwner){

        vm.getInvitationData(invitationInfo).observe(owner) { invitation ->

            playgroundName.text = invitation.slot.playgroundName
            date.text = invitation.slot.date
            val startToEnd = "${invitation.slot.start_time} - ${invitation.slot.end_time}"
            time.text = startToEnd

            invitationItem.setOnClickListener{
                navController.navigate(
                    R.id.action_invitationContainer_to_reservationContainer, bundleOf(
                        "slotID" to invitation.slot.slot_id,
                        "userID" to invitation.slot.user_id,
                        "playgroundID" to invitation.slot.playground_id
                    )
                )
            }

            sportImage.setImageResource(when(invitation.slot.sport){
                "Football" -> R.drawable.sports_soccer_48px
                "Volley" -> R.drawable.sports_volleyball_48px
                "Ping Pong" -> R.drawable.sports_tennis_48px
                "Basket" -> R.drawable.sports_basketball_48px
                else -> R.drawable.selection_background
            })

            val text = "${invitation.sender.nickname} (${invitation.sender.name} ${invitation.sender.surname})"
            sender.text = text
        }


        v.findViewById<Button>(R.id.acceptInvitationButton).setOnClickListener {
            vm.acceptInvitation(invitationInfo)
        }

        v.findViewById<Button>(R.id.declineInvitationButton).setOnClickListener {
            vm.declineInvitation(invitationInfo)
        }
    }
}