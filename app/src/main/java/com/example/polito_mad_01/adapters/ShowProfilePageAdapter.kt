package com.example.polito_mad_01.adapters

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.polito_mad_01.ui.*
import com.example.polito_mad_01.viewmodel.ShowProfileViewModel

class ShowProfilePageAdapter (fragmentActivity: FragmentActivity, private val vm: ShowProfileViewModel): FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> ShowProfile(vm)
                1 -> ShowFriends(vm)
                2 -> FriendRequest(vm)
                else -> throw (Exception("Invalid position"))}
        }

    }