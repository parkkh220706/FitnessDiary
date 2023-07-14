package com.hong_dev.fitnessdiary

import android.view.View
import com.hong_dev.fitnessdiary.databinding.CalendarDayBinding
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = CalendarDayBinding.bind(view).calendarDayText
}