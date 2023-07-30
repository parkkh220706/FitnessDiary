package com.hong_dev.fitnessdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hong_dev.fitnessdiary.databinding.ActivityWeightBinding
import java.text.SimpleDateFormat
import java.util.Locale

class WeightActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeightBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var barChart: BarChart
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.weightDrawerlayout
        auth = FirebaseAuth.getInstance()
        var uid = auth.currentUser!!.uid

        barChart = binding.weightChart

        db.collection("weight")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { result ->
                val weightList = mutableListOf<WeightData>()
                for (document in result) {
                    val weight = document.toObject(WeightData::class.java)
                    weightList.add(weight)
                }
                weightList.sortBy { it.date }
                val weightArray = weightList.toTypedArray()
                val weightArray2 = weightArray.map { it.weight.toFloat() }.toFloatArray()
                val dateArray = weightArray.map { it.date }.toTypedArray()

                fun formatDate(dateString: String): String {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("MM월 dd일", Locale.getDefault())

                    try {
                        val date = inputFormat.parse(dateString)
                        return outputFormat.format(date)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    return dateString // 변환에 실패한 경우 원래 문자열을 반환합니다.
                }

                val formattedDateArray = dateArray.map { formatDate(it) }.toTypedArray()

                val entries = ArrayList<BarEntry>()
                for (i in 0 until weightArray2.size) {
                    entries.add(BarEntry(i.toFloat(), weightArray2[i]))
                }

                val barDataSet = BarDataSet(entries, "체중")
                val data = BarData(barDataSet)
                barChart.data = data

                barDataSet.barBorderWidth = 0.5f
                barDataSet.formSize = 6f

                val xAxis: XAxis = barChart.xAxis
                xAxis.valueFormatter = IndexAxisValueFormatter(formattedDateArray)

                xAxis.position = XAxis.XAxisPosition.TOP

                barChart.invalidate()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show() }


        binding.btnMenu.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(Gravity.LEFT)){
                drawerLayout.openDrawer(Gravity.LEFT)
            } else {
                drawerLayout.closeDrawer(Gravity.LEFT)
            }
        }

        binding.nav2.setNavigationItemSelectedListener {
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

        binding.btnAdd.setOnClickListener {
            val i : Intent = Intent(this, WeightEditActivity::class.java)
            startActivity(i)
        }



    }

    override fun onResume() {
        barChart.notifyDataSetChanged()
        super.onResume()
    }
}