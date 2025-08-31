package com.example.abcd.models

data class Task(
    val id: String = "",
    val title: String = "",
    val time: String = "",
    val note: String = "",
    val isMoved: Boolean = false
)
