package com.example.polito_mad_01.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.ShowProfileViewModel

class FriendRequestAdapter(private val data: List<Pair<String, User>>, private val vm: ShowProfileViewModel) : RecyclerView.Adapter<FriendRequestAdapter.FriendsHolder>() {

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.friend_request_item_layout ,parent, false)
        return FriendsHolder(v,vm)
    }

    override fun onBindViewHolder(holder: FriendsHolder, position: Int) {
        val friend = data[position]
        holder.bind(friend)
    }

    class FriendsHolder(v: View, val vm:ShowProfileViewModel): RecyclerView.ViewHolder(v){
        private val friendId = UIUtils.findTextViewById(v, R.id.friendId)!!
        private val acceptButton = v.findViewById<Button>(R.id.acceptButton)
        private val declineButton = v.findViewById<Button>(R.id.denyButton)


        fun bind(friend: Pair<String, User>){
            val text = "${friend.second.nickname} (${friend.second.name} ${friend.second.surname})"
            friendId.text = text
            acceptButton?.setOnClickListener {
                vm.acceptRequest(friend.first)
            }
            declineButton?.setOnClickListener {
                vm.declineRequest(friend.first)
            }
        }
    }
}
