package com.hong_dev.fitnessdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import com.hong_dev.fitnessdiary.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditBinding
    var date: String = "2022. 07. 06."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        date = intent.getStringExtra("date").toString()
        supportActionBar!!.title = date

        binding.tvDate.setOnClickListener { showTimePickerDialog() }

        binding.btnDone.setOnClickListener { clickDone() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun showTimePickerDialog(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_timepicker, null)
        val numberPicker1 = dialogView.findViewById<NumberPicker>(R.id.numberPicker1)
        val numberPicker2 = dialogView.findViewById<NumberPicker>(R.id.numberPicker2)

        numberPicker1.minValue = 0
        numberPicker1.maxValue = 12
        numberPicker2.minValue = 0
        numberPicker2.maxValue = 59

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->
                val selectedHour = numberPicker1.value
                val selectedMinute = numberPicker2.value
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.tvDate.text = selectedTime


            }
            .setNegativeButton("취소") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
    }

    fun clickDone(){

    }
}