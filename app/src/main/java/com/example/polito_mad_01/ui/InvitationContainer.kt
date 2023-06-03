package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.adapters.InvitationsPageAdapter
import com.example.polito_mad_01.viewmodel.InvitationsViewModel
import com.example.polito_mad_01.viewmodel.InvitationsViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class InvitationContainer : Fragment(R.layout.fragment_invitation_container) {
    lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var invitationPageAdapter: InvitationsPageAdapter

    private val vm: InvitationsViewModel by viewModels {
        InvitationsViewModelFactory(
            (activity?.application as SportTimeApplication).reservationRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.invitationViewPager)
        invitationPageAdapter = InvitationsPageAdapter(requireActivity(), vm)
        viewPager.adapter = invitationPageAdapter

        if(arguments?.getString("goto") == "gameRequests")
            viewPager.setCurrentItem(1, true)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tabLayout.visibility = View.VISIBLE
            when(position){
                0 -> tab.text = "Games"
                1 -> tab.text = "Invitations"
            }
        }.attach()

    }
}