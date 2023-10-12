package com.example.supabasedemo.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable

@Serializable
data class Persons(
    val id: Int,
    val name: String,
    val surname: String?,
    val contacts: Int
)