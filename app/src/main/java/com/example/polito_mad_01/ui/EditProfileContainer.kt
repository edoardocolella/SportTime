package com.example.polito_mad_01.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.polito_mad_01.R
import com.example.polito_mad_01.adapters.ShowProfilePageAdapter
import com.google.android.material.tabs.TabLayout


class EditProfileContainer : Fragment(R.layout.fragment_show_profile_container) {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var showProfilePageAdapter: ShowProfilePageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit_profile, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_save_profile) {
            return true
            //return trySaveData()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.view_pager)
        showProfilePageAdapter = ShowProfilePageAdapter(requireActivity())
        viewPager.adapter = showProfilePageAdapter

        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Do nothing
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do nothing
            }
        })

    }
}