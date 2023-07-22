package com.hong_dev.fitnessdiary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hong_dev.fitnessdiary.databinding.RecyclerviewWorkoutBinding

class UsersDataAdapter(private val usersDataList: List<UsersData>):
    RecyclerView.Adapter<UsersDataAdapter.VH>() {

    class VH(private val binding: RecyclerviewWorkoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(usersData: UsersData){
            binding.apply{
                tvWorkout.text = usersData.exercise.workout
                tvMemo.text = usersData.exercise.memo

                val hours = usersData.exercise.time.substringBefore(":").toInt()
                val minutes = usersData.exercise.time.substringAfter(":").toInt()

                val formattedTime = if (hours == 0 && minutes == 0) {
                    "정보없음"
                } else {
                    "${hours}시간 ${minutes}분"
                }

                tvTime.text = formattedTime
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RecyclerviewWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int {
        return usersDataList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val usersData = usersDataList[position]
        holder.bind(usersData)
    }

}