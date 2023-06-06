package com.example.polito_mad_01.adapters

import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.util.UIUtils


class ParticipantsAdapter (private val data:List<Pair<User,String>>): RecyclerView.Adapter<ParticipantsAdapter.FriendsHolder>(){


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
        private val crown = v.findViewById<ImageView>(R.id.friendItemCrown)
        private val nickname = UIUtils.findTextViewById(v,R.id.friendNickname)!!

        fun bind(friend: Pair<User, String>){
            friendId.text = friend.first.nickname
            val fullName = "${friend.first.name} ${friend.first.surname}"
            nickname.text = fullName
            if (friend.second == "organizer"){
                crown.visibility=View.VISIBLE
            }else{
                crown.visibility = View.GONE
            }
        }
    }


}