package com.example.polito_mad_01.adapters

import android.view.*
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.util.UIUtils


class FriendsAdapter ( private val data:List<Pair<User,String>>,
                       private val navController: NavController
): RecyclerView.Adapter<FriendsAdapter.FriendsHolder>(){


    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.friend_item_layout ,parent, false)
        return FriendsHolder(v, navController)
    }

    override fun onBindViewHolder(holder: FriendsHolder, position: Int) {
        val friend = data[position]
        holder.bind(friend)
    }

    class FriendsHolder(v: View, private val navController: NavController): RecyclerView.ViewHolder(v){
        private val friendId = UIUtils.findTextViewById(v,R.id.friendId)!!
        private val item = v.findViewById<CardView>(R.id.friendItem)
        //private val item = v.findViewById<CardView>(R.id.friendItem)!!


        fun bind(friend: Pair<User,String>) {
            friendId.text = friend.first.nickname

            item.setOnClickListener {
                when (navController.currentDestination?.id) {
                    R.id.showProfileContainer -> {
                        navController.navigate(R.id.action_showProfileContainer_to_showUserProfile)
                    }
                    R.id.findFriendsWithFilters -> {
                        navController.navigate(R.id.action_findFriendsWithFilters_to_showUserProfile)
                    }
                    else -> println("ID ${navController.currentDestination?.displayName} ")
                }
            }
        }
    }
}