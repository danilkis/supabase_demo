package com.example.supabasedemo.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable

@Serializable
data class Contacts(
    val id: Int,
    val phone: String,
    val telegram: String?,
    val url: String?
)