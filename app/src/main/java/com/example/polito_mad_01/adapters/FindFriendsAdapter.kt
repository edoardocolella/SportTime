package com.example.polito_mad_01.adapters

import android.graphics.Color
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
import com.example.polito_mad_01.viewmodel.FindFriendsWithFiltersViewModel

class FindFriendsAdapter (private val users: List<Pair<User,String>>, private val vm: FindFriendsWithFiltersViewModel, private val navController: NavController): RecyclerView.Adapter<FindFriendsAdapter.FindFriendsHolder>() {

    override fun getItemCount() = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindFriendsHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.find_friends_item, parent, false)
        return FindFriendsHolder(v, vm, navController)
    }

    override fun onBindViewHolder(holder: FindFriendsHolder, position: Int) {
        val friend = users[position]
        holder.bind(friend)
    }

    class FindFriendsHolder(private val v: View, val vm: FindFriendsWithFiltersViewModel, private val navController: NavController): RecyclerView.ViewHolder(v) {
        private val friendFullName = UIUtils.findTextViewById(v,R.id.findFriendFullName)!!
        private val friendNickname = UIUtils.findTextViewById(v,R.id.findFriendNickname)!!
        private val addButton: Button = v.findViewById(R.id.findFriendAddButton)

        fun bind(friend: Pair<User, String>){
            friendNickname.text = friend.first.nickname
            val fullName = "${friend.first.name} ${friend.first.surname}"
            friendFullName.text = fullName
            addButton.setOnClickListener {
                vm.sendRequest(friend.first.email)
                addButton.setText(R.string.requestSent)
                addButton.setBackgroundColor(Color.WHITE)
                addButton.setTextColor(Color.BLACK)
                addButton.isClickable=false
            }

            v.findViewById<CardView>(R.id.findFriendsItem).setOnClickListener {
                navController.navigate(R.id.action_findFriendsWithFilters_to_showUserProfile, bundleOf(
                    "userID" to friend.second
                ))
            }

        }
    }
}

