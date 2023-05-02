package com.example.polito_mad_01.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.polito_mad_01.ui.ShowProfile
import com.example.polito_mad_01.ui.ShowSkill

class ShowProfilePageAdapter(fragmentActivity: FragmentActivity,): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ShowProfile()
            1 -> ShowSkill()
            else -> throw (Exception("Invalid position"))}
    }

}