package com.example.polito_mad_01.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.util.UIUtils

class FindFriendsAdapter (private val usersNicknames: List<String>): RecyclerView.Adapter<FindFriendsAdapter.FindFriendsHolder>() {

    override fun getItemCount() = usersNicknames.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindFriendsHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.find_friends_item, parent, false)
        return FindFriendsHolder(v)
    }

    override fun onBindViewHolder(holder: FindFriendsHolder, position: Int) {
        val friend = usersNicknames[position]
        holder.bind(friend)
    }

    class FindFriendsHolder(v: View): RecyclerView.ViewHolder(v) {
        private val friendId = UIUtils.findTextViewById(v,R.id.findFriendId)!!
        private val addButton: Button = v.findViewById(R.id.findFriendAddButton)

        fun bind(friend: String){
            friendId.text = friend
            addButton.setOnClickListener {

            }
        }
    }
}