package com.example.polito_mad_01.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.adapters.FriendsAdapter
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.ShowReservationsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ShowParticipants(val slotID: Int, val vm: ShowReservationsViewModel) : Fragment(R.layout.fragment_show_participants) {

    private lateinit var recyclerViewParticipants: RecyclerView
    private lateinit var noParticipants: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noParticipants = UIUtils.findTextViewById(view, R.id.noParticipantsTextView)!!
        noParticipants.visibility=View.GONE
        recyclerViewParticipants = view.findViewById(R.id.participantsRecyclerView)
        recyclerViewParticipants.layoutManager = LinearLayoutManager(view.context)

        vm.getReservationParticipants(slotID).observe(viewLifecycleOwner) {
            recyclerViewParticipants.adapter= FriendsAdapter(it.map { user -> user.nickname })

            if(it.isEmpty()){
                recyclerViewParticipants.visibility=View.GONE
                noParticipants.visibility=View.VISIBLE
            }else{
                recyclerViewParticipants.visibility=View.VISIBLE
                noParticipants.visibility=View.GONE
            }
        }

        view.findViewById<Button>(R.id.addParticipantsButton).setOnClickListener{
            val friendsMails = vm.getUserFriends().value!!.map { "${it.nickname} (${it.name} ${it.surname})" }.sorted().toTypedArray()
            val selectedFriendsMails = mutableListOf<String>()

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Invite Friends")
                .setMultiChoiceItems(
                    friendsMails, null
                ) { _, which, isChecked ->
                    if (isChecked) {
                        selectedFriendsMails += friendsMails[which]
                    } else if (selectedFriendsMails.contains(friendsMails[which])) {
                        selectedFriendsMails.remove(friendsMails[which])
                    }
                }
                .setPositiveButton("Invite"){ dialog, _ ->
                    // TODO: INVITI a selectedFriendsMail
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}