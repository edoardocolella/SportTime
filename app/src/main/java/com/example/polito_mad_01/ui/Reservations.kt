package com.example.polito_mad_01.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.polito_mad_01.R
import com.example.polito_mad_01.ui.calendar.DayViewContainer
import com.example.polito_mad_01.ui.calendar.MonthViewContainer
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class Reservations : Fragment(R.layout.fragment_reservations) {
    private var selectedDate: LocalDate? = null

    // TODO: Today button, text color changes, badge on top, list of events for each day

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setupCalendar(view: View) {
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        // Month and weekdays definition
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(6)
        val endMonth = currentMonth.plusMonths(6)

        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)
        calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)

        // DayBinder
        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                val textView = container.textView

                // TODO: LocalMonth
                textView.text = data.date.dayOfMonth.toString()
                container.day = data

                // Hide days of other months
                if (data.position != DayPosition.MonthDate) {
                    container.textView.visibility = View.INVISIBLE
                }

                // Set background for selected date
                if (data.date == selectedDate) {
                    textView.setBackgroundResource(R.drawable.selection_background)
                } else {
                    textView.background = null
                }

                container.view.setOnClickListener {
                    if (container.day.position == DayPosition.MonthDate) {

                        val currentSelection = selectedDate
                        if (currentSelection == container.day.date) {
                            // If the user clicks the same date, clear selection.
                            selectedDate = null
                            calendarView.notifyDateChanged(currentSelection)
                        } else {
                            selectedDate = container.day.date
                            calendarView.notifyDateChanged(container.day.date)
                            if (currentSelection != null) {
                                calendarView.notifyDateChanged(currentSelection)
                            }
                        }
                    }
                }
            }
        }

        // MonthHeader
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {

                container.monthTextView.text = data.yearMonth.month.toString()
                container.weekDaysContainer.children.map { it as TextView }
                    .forEachIndexed { index, textView ->
                        val dayOfWeek = daysOfWeek[index]
                        val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        textView.text = title
                    }
            }
        }
    }

}