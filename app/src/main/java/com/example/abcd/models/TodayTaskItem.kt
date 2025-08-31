package com.example.abcd.models

data class TodayTaskItem(
    val task: String,
    val time: String,
    val id: String,
    val isMoved: Boolean,
    val taskDate: Long  // Example extra field for tomorrow tasks
)
