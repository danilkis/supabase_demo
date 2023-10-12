package com.example.supabasedemo.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import java.sql.Timestamp

@Serializable
data class Billing(
    val id: Int,
    val created_at: DateTimePeriod,
    val payment_recieved: DateTimePeriod?, //TODO: Поставить обычный timestamp
    val pay: Float,
    val status: Int
)