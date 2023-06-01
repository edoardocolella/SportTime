package com.example.polito_mad_01.ui

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.example.polito_mad_01.R
import com.example.polito_mad_01.adapters.*
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.ShowProfileViewModel
import com.google.android.material.snackbar.Snackbar


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
                        if(isValidEmail(email)){
                            when(result) {
                                "noAccount" -> Snackbar.make(requireView(), "There is no profile linked to that address", Snackbar.LENGTH_LONG).setAnchorView(R.id.addFriendButton).show()
                                "alreadyFriend" -> Snackbar.make(requireView(), "You are already friends.", Snackbar.LENGTH_LONG).setAnchorView(R.id.addFriendButton).show()
                                else -> Snackbar.make(requireView(), "Request sent successfully.", Snackbar.LENGTH_LONG).setAnchorView(R.id.addFriendButton).show()
                            }
                        }else{
                            Snackbar.make(requireView(), "Email not valid!", Snackbar.LENGTH_LONG).setAnchorView(R.id.addFriendButton).show()
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

fun isValidEmail(target: CharSequence?): Boolean {
    return if (TextUtils.isEmpty(target)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}