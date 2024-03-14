package com.example.supabasedemo.model.Persons

import kotlinx.serialization.Serializable

@Serializable
data class Persons(
    val id: Int,
    val Name: String,
    val Surname: String?,
    val contactsId: Int
)