package com.example.supabasedemo.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable

@Serializable
data class Things(
    val id: Int,
    val name: String,
    val store: String?,
    val amount: Int,
    val type: Int
)