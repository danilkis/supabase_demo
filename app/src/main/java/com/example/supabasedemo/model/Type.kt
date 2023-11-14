package com.example.supabasedemo.model

import kotlinx.serialization.Serializable

@Serializable
data class Type(
    val id: Int,
    val Name: String
)