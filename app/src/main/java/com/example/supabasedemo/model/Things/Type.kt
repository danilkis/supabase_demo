package com.example.supabasedemo.model.Things

import kotlinx.serialization.Serializable

@Serializable
data class Type(
    val id: Int,
    val Name: String
)