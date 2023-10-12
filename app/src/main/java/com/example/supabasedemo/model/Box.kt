package com.example.supabasedemo.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable

@Serializable
data class Box(
    val id: Int,
    val name: String,
    val barcode: String?
)