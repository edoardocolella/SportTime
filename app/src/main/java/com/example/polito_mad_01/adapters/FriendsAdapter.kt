package com.example.polito_mad_01.adapters

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.util.UIUtils


class FriendsAdapter ( private val data:List<User>): RecyclerView.Adapter<FriendsAdapter.FriendsHolder>(){


    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.friend_item_layout ,parent, false)
        return FriendsHolder(v)
    }

    override fun onBindViewHolder(holder: FriendsHolder, position: Int) {
        val friend = data[position]
        holder.bind(friend)
    }

    class FriendsHolder(v: View): RecyclerView.ViewHolder(v){
        private val friendId = UIUtils.findTextViewById(v,R.id.friendId)!!

        fun bind(friend: User){
            friendId.text = friend.nickname
        }
    }


}