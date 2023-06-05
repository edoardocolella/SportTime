package com.example.polito_mad_01.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.FindFriendsWithFiltersViewModel

class FindFriendsAdapter (private val users: List<User>, private val vm: FindFriendsWithFiltersViewModel): RecyclerView.Adapter<FindFriendsAdapter.FindFriendsHolder>() {

    override fun getItemCount() = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindFriendsHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.find_friends_item, parent, false)
        return FindFriendsHolder(v, vm)
    }

    override fun onBindViewHolder(holder: FindFriendsHolder, position: Int) {
        val friend = users[position]
        holder.bind(friend)
    }

    class FindFriendsHolder(v: View, val vm: FindFriendsWithFiltersViewModel): RecyclerView.ViewHolder(v) {
        private val friendFullName = UIUtils.findTextViewById(v,R.id.findFriendFullName)!!
        private val friendNickname = UIUtils.findTextViewById(v,R.id.findFriendNickname)!!
        private val addButton: Button = v.findViewById(R.id.findFriendAddButton)

        fun bind(friend: User){
            friendNickname.text = friend.nickname
            val fullName = "${friend.name} ${friend.surname}"
            friendFullName.text = fullName
            addButton.setOnClickListener {
                vm.sendRequest(friend.email)
                addButton.setText(R.string.requestSent)
                addButton.setBackgroundColor(Color.WHITE)
                addButton.setTextColor(Color.BLACK)
                addButton.isClickable=false
            }
        }
    }
}