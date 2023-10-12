package com.example.supabasedemo.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable

@Serializable
data class Shopping_list(
    val id: Int,
    val thingId: Int,
    val status: Int
)