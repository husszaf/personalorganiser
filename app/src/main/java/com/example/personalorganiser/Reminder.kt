package com.example.personalorganiser

import java.util.*

//Reminder Database data class
data class Reminder(
    val id: Long?,
    val text: String,
    val dateTime: Date
)