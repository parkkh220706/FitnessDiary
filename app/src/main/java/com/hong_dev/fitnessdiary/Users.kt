package com.hong_dev.fitnessdiary

data class ExerciseData(
    var workout: String = "",
    var time: String = "",
    var memo: String = ""
)

data class WeightData(
    var uid: String = "",
    var date: String = "",
    var weight: String = ""
)

data class UsersData(
    var uid: String = "",
    val date: String = "",
    val exercise: ExerciseData = ExerciseData(),
)
