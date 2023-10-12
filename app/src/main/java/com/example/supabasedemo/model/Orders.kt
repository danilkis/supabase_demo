package com.example.supabasedemo.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable

@Serializable
data class Orders(
    val id: Int,
    val created_at: DateTimePeriod,
    val deadline: DateTimePeriod, //TODO: Поставить обычный timestamp
    val billingId: Int,
    val status: Int
)