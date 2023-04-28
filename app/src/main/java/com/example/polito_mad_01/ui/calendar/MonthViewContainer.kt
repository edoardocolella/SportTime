package com.example.polito_mad_01.ui.calendar

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.polito_mad_01.R
import com.kizitonwose.calendar.view.ViewContainer

class MonthViewContainer(view: View) : ViewContainer(view) {
    val monthTextView: TextView = view.findViewById(R.id.monthTextView)
    val yearTextView: TextView = view.findViewById(R.id.yearTextView)
    val weekDaysContainer: ViewGroup = view.findViewById(R.id.weekDaysContainer)

    val prevMonthButton : ImageButton = view.findViewById(R.id.prevMonthButton)
    val nextMonthButton : ImageButton = view.findViewById(R.id.nextMonthButton)
}