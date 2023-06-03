package com.example.polito_mad_01.ui

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.adapters.ReservationPageAdapter
import com.example.polito_mad_01.viewmodel.ShowReservationsViewModel
import com.example.polito_mad_01.viewmodel.ShowReservationsViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.time.LocalDate

class ReservationContainer : Fragment(R.layout.fragment_reservation_container) {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var reservationPageAdapter: ReservationPageAdapter

    private val vm: ShowReservationsViewModel by viewModels {
        ShowReservationsViewModelFactory(
            (activity?.application as SportTimeApplication).reservationRepository,
            (activity?.application as SportTimeApplication).userRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        vm.getReservation(requireArguments().getInt("slotID")).observe(viewLifecycleOwner) {
            if (LocalDate.now().toString() < it.date) {
                inflater.inflate(R.menu.menu_show_reservation, menu)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val args = bundleOf(
            "slotID" to requireArguments().getInt("slotID")
        )

        if (item.itemId == R.id.action_edit_reservation){
            findNavController().navigate(R.id.editReservationFragment, args)
        }
        return true
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