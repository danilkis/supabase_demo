package com.example.supabasedemo.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable

@Serializable
data class Persons(
    val id: Int?,
    val Name: String,
    val Surname: String?,
    val contactsId: Int
)