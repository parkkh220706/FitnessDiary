package com.hong_dev.fitnessdiary

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hong_dev.fitnessdiary.databinding.ActivityMainBinding
import com.hong_dev.fitnessdiary.databinding.CalendarDayBinding
import com.hong_dev.fitnessdiary.databinding.CalendarHeaderBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
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
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerview: RecyclerView
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    private var selectedDate: LocalDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvWorkout.layoutManager = LinearLayoutManager(this)

        recyclerview = binding.rvWorkout
        drawerLayout = binding.mainDrawerLayout
        calendarView = binding.OneDayCalendar
        auth = FirebaseAuth.getInstance()
        var uid = auth.currentUser!!.uid

/*        db.collection(uid).get()
            .addOnSuccessListener { result ->
                val userDataList = mutableListOf<UsersData>()
                for (document in result) {
                    val exerciseData = document.toObject(UsersData::class.java)
                    userDataList.add(exerciseData)
                }
                val adapter = UsersDataAdapter(userDataList)
                recyclerview.adapter = adapter
            }
            .addOnFailureListener() { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }*/



        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView = CalendarDayBinding.bind(view).calendarDayText

        }

        class MonthViewContainer(view: View) : ViewContainer(view){
            val binding = CalendarHeaderBinding.bind(view)
        }

        binding.btnMenu.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(Gravity.LEFT)){
                drawerLayout.openDrawer(Gravity.LEFT)
            } else {
                drawerLayout.closeDrawer(Gravity.LEFT)
            }
        }

        binding.nav.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_weight -> {
                    val intent = Intent(this, WeightActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

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

                    db.collection(uid)
                        .whereEqualTo("date", selectedDate.toString())
                        .get()
                        .addOnSuccessListener { result ->
                            val userDataList = mutableListOf<UsersData>()
                            for (document in result) {
                                val exerciseData = document.toObject(UsersData::class.java)
                                userDataList.add(exerciseData)
                            }
                            val adapter = UsersDataAdapter(userDataList)
                            recyclerview.adapter = adapter
                        }
                        .addOnFailureListener() { exception ->
                            Log.w(TAG, "Error getting documents.", exception)
                        }
                    binding.tv.text = selectedDate.toString()
                }


            }


        }

        binding.btnAdd.setOnClickListener {
            val i = Intent(this, EditActivity::class.java)
            i.putExtra("date", selectedDate.toString())
            startActivity(i)
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

/*        val docRef = db.collection("Users").get()
            .addOnSuccessListener { documentSnapshot ->
                val selectUser = documentSnapshot.toObjects(UsersData::class.java)
                Log.d("TAG", selectUser.toString())
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }*/



    }

    fun updateTitle(){
        val month = binding.OneDayCalendar.findFirstVisibleMonth()?.yearMonth ?: return
        binding.YearText.text = month.year.toString()
        binding.MonthText.text = month.month.name
    }

    override fun onResume() {
        super.onResume()
        binding.rvWorkout.adapter?.notifyDataSetChanged()
    }



}