package com.example.polito_mad_01.ui.calendar

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.polito_mad_01.R
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate

class DayViewContainer(view: View) : ViewContainer(view){
    val textView: TextView = view.findViewById(R.id.calendarDayText)

    lateinit var day: CalendarDay

    fun showBadge(){
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,R.drawable.calendar_badge)
    }
}