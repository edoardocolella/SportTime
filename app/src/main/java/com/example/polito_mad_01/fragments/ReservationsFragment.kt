package com.example.polito_mad_01.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.example.polito_mad_01.R
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import java.text.SimpleDateFormat
import java.util.*

class ReservationsFragment : Fragment(R.layout.fragment_reservations) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView: CustomCalendarView = view.findViewById(R.id.calendar_view)
        val currentCalendar: Calendar = Calendar.getInstance(Locale.getDefault())

        calendarView.setFirstDayOfWeek(Calendar.MONDAY)
        calendarView.setShowOverflowDate(false)
        calendarView.refreshCalendar(currentCalendar)

        calendarView.setCalendarListener(object : CalendarListener {
            override fun onDateSelected(date: Date?) {
                val df = SimpleDateFormat("dd-MM-yyyy")
                Toast.makeText(view.context, df.format(date), Toast.LENGTH_SHORT).show()
            }

            override fun onMonthChanged(date: Date?) {
                val df = SimpleDateFormat("MM-yyyy")
                Toast.makeText(view.context, df.format(date), Toast.LENGTH_SHORT).show()
            }
        })
    }


}