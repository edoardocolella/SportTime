package com.example.polito_mad_01.adapters

import android.view.*
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.util.UIUtils


class ParticipantsAdapter (private val data:List<Pair<User,String>>,
                           private val navController: NavController,
                           private val slotOrganizer: String): RecyclerView.Adapter<ParticipantsAdapter.FriendsHolder>(){


    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.friend_item_layout ,parent, false)
        return FriendsHolder(v, navController, slotOrganizer)
    }

    override fun onBindViewHolder(holder: FriendsHolder, position: Int) {
        val friend = data[position]
        holder.bind(friend)
    }

    class FriendsHolder(v: View, private val navController: NavController, private val slotOrganizer: String): RecyclerView.ViewHolder(v){
        private val friendId = UIUtils.findTextViewById(v,R.id.friendId)!!
        private val friendNickname = UIUtils.findTextViewById(v,R.id.friendNickname)!!
        private val friendItem = v.findViewById<CardView>(R.id.friendItem)
        private val crown = v.findViewById<ImageView>(R.id.friendItemCrown)


        fun bind(friend: Pair<User, String>){
            val text = "${friend.first.name} ${friend.first.surname}"
            friendId.text = text
            friendNickname.text = friend.first.nickname

            if (friend.second == slotOrganizer){
                crown.visibility=View.VISIBLE
            }else{
                crown.visibility = View.GONE
            }

            friendItem.setOnClickListener {
                navController.navigate(R.id.action_reservationContainer_to_showUserProfile,
                    bundleOf("userId" to friend.second)
                )
            }
        }
    }


}