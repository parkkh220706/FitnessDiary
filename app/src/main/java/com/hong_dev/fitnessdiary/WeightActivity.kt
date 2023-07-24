package com.hong_dev.fitnessdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.hong_dev.fitnessdiary.databinding.ActivityWeightBinding

class WeightActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeightBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLineChart(binding.chart)

        drawerLayout = binding.weightDrawerlayout

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

    private fun initLineChart(lineChart: LineChart){

        lineChart.setDrawGridBackground(false)
        lineChart.setDrawBorders(false)

        val description = Description()
        description.isEnabled = false
        lineChart.description = description

        lineChart.animateY(1000)
        lineChart.animateX(1000)

        val xAxis: XAxis = lineChart.xAxis

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.textColor = R.color.black

    }

   /* private fun showWeightDialog(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_weightpicker, null)
        val weightPicker1 = dialogView.findViewById<NumberPicker>(R.id.WeightPicker1)
        val weightPicker2 = dialogView.findViewById<NumberPicker>(R.id.WeightPicker2)

        weightPicker1.minValue = 0
        weightPicker1.maxValue = 150
        weightPicker2.minValue = 0
        weightPicker2.maxValue = 9
        weightPicker1.value =50

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->

                val selectedKg = weightPicker1.value
                val selectedweight = weightPicker2.value
                val selectedTime = String.format("%02d.%02d kg", selectedKg, selectedweight)


            }
            .setNegativeButton("취소") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }*/
}