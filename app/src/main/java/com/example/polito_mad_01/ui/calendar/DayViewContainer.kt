package com.example.polito_mad_01.ui.calendar

import android.view.View
import android.widget.TextView
import com.example.polito_mad_01.R
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view){
    val textView: TextView = view.findViewById(R.id.calendarDayText)
    var hasReservation = false
    var hasFreeSlot = false

    lateinit var day: CalendarDay

    fun showReservationBadge(){
        if(hasFreeSlot){
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,R.drawable.calendar_double_badge)
        } else {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,R.drawable.calendar_reservation_badge)
        }
        hasReservation = true
    }

    fun showFreeBadge(){
        if(hasReservation){
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,R.drawable.calendar_double_badge)
        } else {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,R.drawable.calendar_free_badge)
        }

        hasFreeSlot = true
    }
}