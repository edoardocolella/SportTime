package com.example.polito_mad_01.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.polito_mad_01.ui.*
import com.example.polito_mad_01.viewmodel.InvitationsViewModel

class InvitationsPageAdapter(fragmentActivity: FragmentActivity, val vm: InvitationsViewModel): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ShowGames()
            1 -> ShowInvitations()
            else -> throw (Exception("Invalid position"))}
    }
}