package com.example.polito_mad_01.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.adapters.ReservationPageAdapter
import com.example.polito_mad_01.viewmodel.ShowReservationsViewModel
import com.example.polito_mad_01.viewmodel.ShowReservationsViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ReservationContainer : Fragment(R.layout.fragment_reservation_container) {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var reservationPageAdapter: ReservationPageAdapter

    private val vm: ShowReservationsViewModel by viewModels {
        ShowReservationsViewModelFactory(
            (activity?.application as SportTimeApplication).reservationRepository,
            (activity?.application as SportTimeApplication).userRepository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slotID = requireArguments().getInt("slotID")

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.reservationsViewPager)
        reservationPageAdapter = ReservationPageAdapter(requireActivity(), slotID, vm)
        viewPager.adapter = reservationPageAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tabLayout.visibility = View.VISIBLE
            when(position){
                0 -> tab.text = "Reservation"
                1 -> tab.text = "Participants"
            }
        }.attach()

    }
}