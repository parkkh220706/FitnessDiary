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
                tvWorkout.text = "Workout: ${usersData.exercise.workout}"
                tvTime.text = "Time: ${usersData.exercise.workout}"
                tvMemo.text = "Memo: ${usersData.exercise.memo}"
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