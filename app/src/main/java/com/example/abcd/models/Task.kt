package com.example.abcd.models

import java.util.Date

data class Task(
    val id: String = "",
    val text: String = "",
    val scheduledDate: Date = Date(),
    val createdAt: Date = Date(),
    val isCompleted: Boolean = false,
    val isDeleted: Boolean = false
)
