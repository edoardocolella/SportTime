package com.example.polito_mad_01.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.polito_mad_01.model.Slot
import com.example.polito_mad_01.ui.BlankReservations
import com.example.polito_mad_01.ui.FreeSlotList
import com.example.polito_mad_01.ui.ReservationList

class DaySlotAdapter(fragment: Fragment, private val slotList : List<Slot>) : FragmentStateAdapter(fragment) {
    enum class DayType {
        NONE,
        ONLY_RESERVATIONS,
        ONLY_FREE_SLOTS,
        BOTH,
    }

    var dayType : DayType = when {
        slotList.isEmpty() -> DayType.NONE
        slotList.none { it.user_id == null } -> DayType.ONLY_RESERVATIONS
        slotList.none { it.user_id != null } -> DayType.ONLY_FREE_SLOTS
        else -> DayType.BOTH
    }

    override fun getItemCount(): Int = when(dayType){
        DayType.NONE -> 1
        DayType.ONLY_RESERVATIONS -> 1
        DayType.ONLY_FREE_SLOTS -> 1
        DayType.BOTH -> 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(dayType){
            DayType.NONE -> BlankReservations()
            DayType.ONLY_RESERVATIONS -> ReservationList(slotList.filter { it.user_id != null })
            DayType.ONLY_FREE_SLOTS -> FreeSlotList(slotList.filter { it.user_id == null })
            DayType.BOTH -> when(position){
                0 -> ReservationList(slotList.filter { it.user_id != null })
                1 -> FreeSlotList(slotList.filter { it.user_id == null })
                else -> throw (Exception("Invalid position"))
            }
        }
    }

}