package com.hong_dev.fitnessdiary

import android.content.ContentValues
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hong_dev.fitnessdiary.databinding.ActivityWeightEditBinding
import com.hong_dev.fitnessdiary.databinding.CalendarDayBinding
import com.hong_dev.fitnessdiary.databinding.CalendarHeaderBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class WeightEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeightEditBinding
    private lateinit var calendarView: com.kizitonwose.calendar.view.CalendarView
    private var selectedDate: LocalDate? = null
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeightEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        calendarView = binding.WeightCalendarView

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        auth = FirebaseAuth.getInstance()
        var uid = auth.currentUser!!.uid


        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView = CalendarDayBinding.bind(view).calendarDayText

        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val binding = CalendarHeaderBinding.bind(view)
        }

        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(240)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(240)  // Adjust as needed
        val firstDayOfWeek =
            WeekFields.of(Locale.getDefault()).firstDayOfWeek // Available from the library
        calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)

        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                val textView = container.textView
                textView.text = data.date.dayOfMonth.toString()
                calendarView.isClickable = true

                if (data.position == DayPosition.MonthDate) {
                    textView.setTextColor(Color.BLACK)
                    container.view.setBackgroundResource(if (selectedDate == data.date) R.drawable.bg_selected else 0)
                } else {
                    textView.setTextColor(Color.GRAY)
                    container.view.background = null
                }

                container.textView.setOnClickListener {
                    if (data.position == DayPosition.MonthDate && selectedDate != data.date) {
                        val oldSelectedDate = selectedDate
                        selectedDate = data.date

                        // 이전에 선택된 날짜의 배경을 제거합니다.
                        oldSelectedDate?.let { calendarView.notifyDateChanged(it) }

                        // 새로 선택된 날짜와 이전에 선택된 날짜에 대한 배경을 설정합니다.
                        calendarView.notifyDateChanged(data.date)
                        if (oldSelectedDate != null) {
                            calendarView.notifyDateChanged(oldSelectedDate)
                        }
                    }
                    // 뭔가 해야하면 여기서..


                }


            }

        }

        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                if (container.binding.root.tag == null) {
                    container.binding.root.tag = data.yearMonth
                    container.binding.legendLayout.root.children.map { it as TextView }
                        .forEachIndexed { index, tv ->
                            tv.text = daysOfWeek[index].getDisplayName(
                                TextStyle.SHORT,
                                Locale.getDefault()
                            )
                        }
                }
            }

            override fun create(view: View) = MonthViewContainer(view)

        }

        calendarView.monthScrollListener = {
            updateTitle()
        }

        val WeightData = mapOf(
            "weight" to binding.etWeight.text.toString(),
            "date" to selectedDate.toString()
        )

        binding.btnDone.setOnClickListener {
            var date = selectedDate.toString()
            var weight = binding.etWeight.text.toString()

            var weightData = WeightData(uid, date, weight)

           /* db.collection(uid)
                .add(weightData)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                }*/

            db.collection("weight")
                .add(weightData)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }


    fun updateTitle() {
        val month = binding.WeightCalendarView.findFirstVisibleMonth()?.yearMonth ?: return
        binding.YearText.text = month.year.toString()
        binding.MonthText.text = month.month.name
    }
}


