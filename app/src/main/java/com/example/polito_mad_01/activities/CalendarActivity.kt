package com.example.polito_mad_01.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.polito_mad_01.R
import com.stacktips.view.CalendarListener
import com.stacktips.view.CustomCalendarView
import java.text.SimpleDateFormat
import java.util.*


class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        var calendarView: CustomCalendarView = findViewById(R.id.calendar_view)
        val currentCalendar: Calendar = Calendar.getInstance(Locale.getDefault())

        calendarView.setFirstDayOfWeek(Calendar.MONDAY)
        calendarView.setShowOverflowDate(false)
        calendarView.refreshCalendar(currentCalendar)

        calendarView.setCalendarListener(object : CalendarListener {
            override fun onDateSelected(date: Date?) {
                val df = SimpleDateFormat("dd-MM-yyyy")
                Toast.makeText(this@CalendarActivity, df.format(date), Toast.LENGTH_SHORT).show()
            }

            override fun onMonthChanged(date: Date?) {
                val df = SimpleDateFormat("MM-yyyy")
                Toast.makeText(this@CalendarActivity, df.format(date), Toast.LENGTH_SHORT).show()
            }
        })
    }
}