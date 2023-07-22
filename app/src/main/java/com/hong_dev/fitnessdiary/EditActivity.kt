package com.hong_dev.fitnessdiary

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hong_dev.fitnessdiary.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditBinding
    var date: String = ""

    lateinit var auth: FirebaseAuth
    var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        auth = FirebaseAuth.getInstance()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        date = intent.getStringExtra("date").toString()

        /*        val userId = auth.currentUser?.uid
        if(userId != null){
            val userCollectionRef = firestore.collection("Users").document(userId)
        }*/

        supportActionBar!!.title = date

        binding.tvDate.setOnClickListener { showTimePickerDialog() }

        binding.btnDone.setOnClickListener {

            var uid = auth.currentUser!!.uid
            val date = intent.getStringExtra("date").toString()

            var workout = binding.tvWorkout.text.toString()
            var time = binding.tvDate.text.toString()
            var memo = binding.tvMemo.text.toString()

            var exerciseData = ExerciseData(workout, time, memo)
            var usersData = UsersData(uid, date, exerciseData )

            db.collection(uid)
                .add(usersData)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                }
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    /*fun showWeightPickerDialog(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_weightpicker, null)
        val weightPicker = dialogView.findViewById<NumberPicker>(R.id.weight_picker)

        weightPicker.minValue = 0
        weightPicker.maxValue = 200

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->
                val selectedWeight = weightPicker.value
                binding.tvWeight.text = selectedWeight.toString()
            }
            .setNegativeButton("취소") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }*/

    fun showTimePickerDialog() {
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
}




