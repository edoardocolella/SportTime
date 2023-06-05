package com.example.polito_mad_01.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.ShowProfileViewModel
import com.google.android.material.snackbar.Snackbar

class FriendRequestAdapter(private val data: List<Pair<String, User>>,
                           private val vm: ShowProfileViewModel,
                            private val navController: NavController
                           ) : RecyclerView.Adapter<FriendRequestAdapter.FriendsHolder>() {

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.friend_request_item_layout ,parent, false)
        return FriendsHolder(v,vm, navController)
    }

    override fun onBindViewHolder(holder: FriendsHolder, position: Int) {
        val friend = data[position]
        holder.bind(friend)
    }

    class FriendsHolder(private val v: View, val vm:ShowProfileViewModel, private val navController: NavController): RecyclerView.ViewHolder(v){
        private val friendId = UIUtils.findTextViewById(v, R.id.friendId)!!
        private val acceptButton = v.findViewById<Button>(R.id.acceptButton)
        private val declineButton = v.findViewById<Button>(R.id.denyButton)
        private val cardView = v.findViewById<CardView>(R.id.friendItem)


        fun bind(friend: Pair<String, User>){
            val text = "${friend.second.nickname} (${friend.second.name} ${friend.second.surname})"

            friendId.text = text

            cardView.setOnClickListener {
                navController.navigate(R.id.action_showProfileContainer_to_showUserProfile,
                bundleOf("isRequest" to true, "userId" to friend.first)
                )
            }

            acceptButton?.setOnClickListener {
                vm.acceptRequest(friend.first)
                Snackbar.make(v, "Friend request accepted", Snackbar.LENGTH_LONG).show()
            }
            declineButton?.setOnClickListener {
                vm.declineRequest(friend.first)
                Snackbar.make(v, "Friend request declined", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
