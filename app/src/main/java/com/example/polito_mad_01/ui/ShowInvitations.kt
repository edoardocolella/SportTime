package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.adapters.FriendRequestAdapter
import com.example.polito_mad_01.adapters.InvitationAdapter
import com.example.polito_mad_01.util.UIUtils
import com.example.polito_mad_01.viewmodel.InvitationsViewModel

class ShowInvitations(val vm: InvitationsViewModel) : Fragment(R.layout.fragment_show_invitations) {
    private lateinit var recyclerViewGameInvitations: RecyclerView
    private lateinit var noInvitations: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*        noInvitations = UIUtils.findTextViewById(view, R.id.noInvitationTextView)!!
        noInvitations.visibility= View.GONE*/
        recyclerViewGameInvitations =view.findViewById(R.id.invitationList)
        recyclerViewGameInvitations.layoutManager = LinearLayoutManager(view.context)


        setAllView()
    }

    private fun setAllView() {
        vm.getUserInvitations().observe(viewLifecycleOwner) { invitations ->
            recyclerViewGameInvitations.adapter= InvitationAdapter(invitations)

            if (invitations.isEmpty()) {
                recyclerViewGameInvitations.visibility = View.GONE
                //noInvitations.visibility = View.VISIBLE
            } else {
                recyclerViewGameInvitations.visibility = View.VISIBLE
                //noInvitations.visibility = View.GONE
            }
        }
    }
}