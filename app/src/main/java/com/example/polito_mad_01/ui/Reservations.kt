package com.example.polito_mad_01.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polito_mad_01.R
import com.example.polito_mad_01.SportTimeApplication
import com.example.polito_mad_01.adapters.ReservationAdapter
import com.example.polito_mad_01.db.Slot
import com.example.polito_mad_01.db.SlotWithPlayground
import com.example.polito_mad_01.ui.calendar.DayViewContainer
import com.example.polito_mad_01.ui.calendar.MonthViewContainer
import com.example.polito_mad_01.viewmodel.ReservationsViewModel
import com.example.polito_mad_01.viewmodel.ReservationsViewModelFactory
import com.kizitonwose.calendar.core.*
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import java.text.DateFormatSymbols
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.HashMap

class Reservations : Fragment(R.layout.fragment_reservations) {
    private var selectedDate: LocalDate? = null

    lateinit var recyclerView: RecyclerView
    lateinit var noReservations : TextView

    private var reservationMap : MutableMap<String, List<SlotWithPlayground>> = mutableMapOf()

    // TODO: Today button, arrow button for months, padding, text color changes, badge on top, list of events for each day

    private val vm: ReservationsViewModel by viewModels {
        ReservationsViewModelFactory((activity?.application as SportTimeApplication).reservationRepository)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reservationMap = mutableMapOf()
        selectedDate = LocalDate.now()

        noReservations = view.findViewById(R.id.no_reservations)
        recyclerView = view.findViewById(R.id.reservationList)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // p1
        setupList(view)

        // p3
        setupCalendar(view)
    }


    fun setupList(view: View){
        vm.getUserReservations(1).observe(viewLifecycleOwner){ list ->
            list.forEach {
                val date = it.slot.date
                val reservations = reservationMap.getOrDefault(date, listOf())

                reservationMap[date] = reservations.plus(it)
            }
        }
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
        selectedDate?.let {
            calendarView.notifyDateChanged(it)
        }

        // DayBinder
        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                val textView = container.textView

                textView.text = data.date.dayOfMonth.toString()
                container.day = data

                vm.getUserReservations(1).observe(viewLifecycleOwner) { list ->
                    val formattedDate = data.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    if(list.map { it.slot.date }.contains(data.date.toString())){
                        container.showBadge()
                    }

                }

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

                            recyclerView.visibility = View.GONE
                            noReservations.visibility = View.VISIBLE
                            //recyclerView.adapter = ReservationAdapter(listOf(), findNavController())
                        } else {
                            selectedDate = container.day.date
                            calendarView.notifyDateChanged(container.day.date)

                            val dateString = selectedDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            val datedList = reservationMap[dateString]


                            if(datedList == null || datedList?.size == 0){
                                recyclerView.visibility = View.GONE
                                noReservations.visibility = View.VISIBLE
                            } else {
                                recyclerView.visibility = View.VISIBLE
                                noReservations.visibility = View.GONE
                                recyclerView.adapter = ReservationAdapter(datedList?: listOf(), findNavController())
                            }


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

                container.monthTextView.text = DateFormatSymbols().months[data.yearMonth.monthValue-1]
                container.yearTextView.text = data.yearMonth.year.toString()

                container.prevMonthButton.setOnClickListener {
                    calendarView.smoothScrollToMonth(data.yearMonth.previousMonth)
                }

                container.nextMonthButton.setOnClickListener {
                    calendarView.smoothScrollToMonth(data.yearMonth.nextMonth)
                }

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