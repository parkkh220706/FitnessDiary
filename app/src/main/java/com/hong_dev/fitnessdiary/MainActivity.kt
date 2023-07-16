package com.hong_dev.fitnessdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.hong_dev.fitnessdiary.databinding.ActivityMainBinding
import com.hong_dev.fitnessdiary.databinding.CalendarDayBinding
import com.hong_dev.fitnessdiary.databinding.CalendarHeaderBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var calendarView: com.kizitonwose.calendar.view.CalendarView


    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView = CalendarDayBinding.bind(view).calendarDayText
        }

        class MonthViewContainer(view: View) : ViewContainer(view){
            val binding = CalendarHeaderBinding.bind(view)
        }

        binding.btnMenu.setOnClickListener {
            if (!binding.drawerLayout.isDrawerOpen(Gravity.LEFT)){
                binding.drawerLayout.openDrawer(Gravity.LEFT)
            } else {
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
            }
        }






        calendarView = binding.OneDayCalendar

        calendarView.monthScrollListener = {
            updateTitle()
        }

        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(240)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(240)  // Adjust as needed
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek // Available from the library
        calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)

        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
            }
        }

        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer>{
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                if (container.binding.root.tag == null){
                    container.binding.root.tag = data.yearMonth
                    container.binding.legendLayout.root.children.map { it as TextView }.forEachIndexed { index, tv ->
                        tv.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    }
                }
            }   

            override fun create(view: View) = MonthViewContainer(view)

        }



    }

    fun updateTitle(){
        val month = binding.OneDayCalendar.findFirstVisibleMonth()?.yearMonth ?: return
        binding.YearText.text = month.year.toString()
        binding.MonthText.text = month.month.name
    }
}