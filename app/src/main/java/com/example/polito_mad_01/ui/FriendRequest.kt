package com.example.polito_mad_01.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.adapters.FriendRequestAdapter
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.ShowProfileViewModel

class FriendRequest(private val vm: ShowProfileViewModel) : Fragment(R.layout.fragment_friend_request) {

    private lateinit var recyclerViewFriendRequests: RecyclerView
    private lateinit var noFriends: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noFriends = UIUtils.findTextViewById(view, R.id.noRequestsTextView)!!
        noFriends.visibility=View.GONE
        recyclerViewFriendRequests =view.findViewById(R.id.friendRequestsRecyclerView)
        recyclerViewFriendRequests.layoutManager = LinearLayoutManager(view.context)


        setAllView()
    }

    private fun setAllView() {



        vm.getFriendRequests().observe(viewLifecycleOwner) { friendRequests ->
            vm.getFriendsRequestsNickname(friendRequests).observe(viewLifecycleOwner) { friendRequestsNickname ->



                recyclerViewFriendRequests.adapter= FriendRequestAdapter(friendRequestsNickname, vm)


                if (friendRequestsNickname.isEmpty()) {
                    recyclerViewFriendRequests.visibility = View.GONE
                    noFriends.visibility = View.VISIBLE
                } else {
                    recyclerViewFriendRequests.visibility = View.VISIBLE
                    noFriends.visibility = View.GONE
                }
            }
        }
    }


}