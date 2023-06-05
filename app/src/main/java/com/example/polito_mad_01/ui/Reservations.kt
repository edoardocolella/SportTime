package com.example.polito_mad_01.ui

import android.os.*
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.fragment.app.*
import androidx.recyclerview.widget.*
import androidx.viewpager2.widget.ViewPager2
import com.example.polito_mad_01.*
import com.example.polito_mad_01.R
import com.example.polito_mad_01.adapters.*
import com.example.polito_mad_01.model.Slot
import com.example.polito_mad_01.ui.calendar.*
import com.example.polito_mad_01.viewmodel.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kizitonwose.calendar.core.*
import com.kizitonwose.calendar.view.*
import java.text.DateFormatSymbols
import java.time.*
import java.time.format.*
import java.util.*

class Reservations : Fragment(R.layout.fragment_reservations) {
    //private var selectedDate: LocalDate? = null

    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager : ViewPager2

    private var reservationMap : MutableMap<String, List<Slot>> = mutableMapOf()

    private val vm: ReservationsViewModel by viewModels {
        ReservationsViewModelFactory((activity?.application as SportTimeApplication).reservationRepository)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reservationMap = mutableMapOf()
        val selectedDateString = arguments?.getString("selectedDate")
        //vm.selectedDate = if(selectedDateString != null) LocalDate.parse(selectedDateString) else LocalDate.now()


        tabLayout =  view.findViewById(R.id.reservationTabLayout)
        viewPager = view.findViewById(R.id.reservationViewPager)
        viewPager.adapter = DaySlotAdapter(this, listOf())

        // serve solo a creare un dataset
        //vm.createSlots()

        // p1
        setupList(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupList(view: View){
        vm.getUserSlots().observe(viewLifecycleOwner){ list ->
            reservationMap = mutableMapOf()
            list.forEach {

                val date = it.date
                val reservations = reservationMap.getOrDefault(date, listOf())

                reservationMap[date] = reservations.plus(it)
            }

            setList()
            setupCalendar(view)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setupCalendar(view: View) {
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        // Month and weekdays definition
        val currentMonth = YearMonth.now()
        val endMonth = currentMonth.plusMonths(6)

        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)
        calendarView.setup(currentMonth, endMonth, daysOfWeek.first())
        vm.selectedDate?.let {
            calendarView.scrollToMonth(it.yearMonth)
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

                container.hideBadges()
                val slots = reservationMap[data.date.toString()]

                if(slots?.any { it.reserved } == true)
                    container.showFreeBadge()

                if(slots?.any { !it.reserved } == true)
                    container.showReservationBadge()

                // Hide days of other months
                if (data.position != DayPosition.MonthDate) {
                    container.textView.visibility = View.INVISIBLE
                } else {
                    container.textView.visibility = View.VISIBLE
                }

                // Set background for selected date
                if (data.date == vm.selectedDate) {
                    textView.setBackgroundResource(R.drawable.selection_background)
                } else {
                    textView.background = null
                }

                container.view.setOnClickListener {
                    if (container.day.position == DayPosition.MonthDate) {

                        val currentSelection = vm.selectedDate
                        if (currentSelection == container.day.date) {
                            // If the user clicks the same date, clear selection.
                            vm.selectedDate = null
                            calendarView.notifyDateChanged(currentSelection)
                            setList()

                        } else {
                            vm.selectedDate = container.day.date
                            calendarView.notifyDateChanged(container.day.date)

                            setList()

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
                container.monthTextView.text = DateFormatSymbols().months[data.yearMonth.monthValue-1].mapIndexed {
                    index, letter -> if(index == 0) letter.uppercaseChar() else letter
                }.joinToString("")
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
    @RequiresApi(Build.VERSION_CODES.O)
    fun setList(){
        val dateString = vm.selectedDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val slotList = reservationMap[dateString] ?: listOf()

        viewPager.adapter = DaySlotAdapter(this, slotList)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tabLayout.visibility = View.VISIBLE
            when {
                slotList.isEmpty() -> tabLayout.visibility = View.INVISIBLE
                slotList.none { it.user_id == null } -> tab.text ="Reservations"
                slotList.none { it.user_id != null } -> tab.text ="Free slots"
                else -> when(position){
                    0 -> tab.text = "Reservations"
                    1 -> tab.text = "Free slots"
                }
            }
        }.attach()
    }
}