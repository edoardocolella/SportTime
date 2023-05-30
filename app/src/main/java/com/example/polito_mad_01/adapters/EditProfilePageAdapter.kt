package com.example.polito_mad_01.adapters

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.polito_mad_01.ui.*
import com.example.polito_mad_01.viewmodel.EditProfileViewModel

class EditProfilePageAdapter(
    fragmentActivity: FragmentActivity,
    private val vm: EditProfileViewModel
    ): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> EditProfile(vm)
            1 -> EditSkill(vm)
            else -> throw (Exception("Invalid position"))}
    }

}