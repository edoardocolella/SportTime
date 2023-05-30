package com.example.polito_mad_01.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.adapters.*
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.ShowProfileViewModel

class ShowFriends(private val vm :ShowProfileViewModel) : Fragment(R.layout.fragment_show_friends) {

    private lateinit var recyclerViewFriends: RecyclerView
    private lateinit var noFriends: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {}.isEnabled = false

        noFriends = UIUtils.findTextViewById(view, R.id.noFriendsTextView)!!
        noFriends.visibility=View.GONE
        recyclerViewFriends =view.findViewById(R.id.friendsRecyclerView)
        recyclerViewFriends.layoutManager = LinearLayoutManager(view.context)

        vm.getUser().observe(viewLifecycleOwner){ user ->

            val friends = user.friends

            vm.getFriends(friends).observe(viewLifecycleOwner){ friendsPair ->
                val friendsNick = friendsPair.map{it.second}
                recyclerViewFriends.adapter= FriendsAdapter(friendsNick)

            }

            if(friends.isEmpty()){
                recyclerViewFriends.visibility=View.GONE
                noFriends.visibility=View.VISIBLE
            }else{
                recyclerViewFriends.visibility=View.VISIBLE
                noFriends.visibility=View.GONE
            }
        }

        view.findViewById<Button>(R.id.addFriendButton).setOnClickListener{
           AlertDialog.Builder(requireContext())
                .setTitle("Add Friend")
                .setMessage("Insert the email of the user you want to add")
                .setView(R.layout.dialog_add_friend)
                .setPositiveButton("Add"){ dialog, _ ->
                    val email = (dialog as AlertDialog).findViewById<TextView>(R.id.emailEditText)?.text.toString()
                    vm.addFriend(email).observe(viewLifecycleOwner){ result ->
                        println("RESULT $result")
                        if(result.isEmpty()){
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(), "Request sent", Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

    }
}