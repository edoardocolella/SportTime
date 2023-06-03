package com.example.polito_mad_01.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.Invitation
import com.example.polito_mad_01.util.UIUtils

class InvitationAdapter( private val data:List<Invitation>): RecyclerView.Adapter<InvitationHolder>(){

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.invitation_item_layout ,parent, false)
        return InvitationHolder(v)
    }

    override fun onBindViewHolder(holder: InvitationHolder, position: Int) {
        val invitation = data[position]
        holder.bind(invitation)
    }
}

class InvitationHolder(v: View): RecyclerView.ViewHolder(v){
    //private val sender = UIUtils.findTextViewById(v, R.id.senderTextView)!!

    fun bind(invitation: Invitation){
        //sender.text = invitation.sender.name
    }
}