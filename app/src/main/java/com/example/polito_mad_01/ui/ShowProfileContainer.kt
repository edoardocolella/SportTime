package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.adapters.ShowProfilePageAdapter
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.tabs.*

class ShowProfileContainer : Fragment(R.layout.fragment_show_profile_container) {

        lateinit var tabLayout: TabLayout
        lateinit var viewPager: ViewPager2
        lateinit var showProfilePageAdapter: ShowProfilePageAdapter

        private val vm: ShowProfileViewModel by viewModels {
            ShowProfileViewModelFactory((activity?.application as SportTimeApplication).userRepository)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if(savedInstanceState == null)
                setHasOptionsMenu(true)
        }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_show_profile, menu)
    }


    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_save_profile)
            findNavController().navigate(R.id.editProfileContainer)
        return true
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            tabLayout = view.findViewById(R.id.tabLayout)
            viewPager = view.findViewById(R.id.reservationsViewPager)
            showProfilePageAdapter = ShowProfilePageAdapter(requireActivity(), vm)
            viewPager.adapter = showProfilePageAdapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tabLayout.visibility = View.VISIBLE
                when(position){
                    0 -> tab.text = "Profile"
                    1 -> tab.text = "Friends"
                    //2 -> tab.text = "Requests"
                }
            }.attach()

        }
    }
