package com.example.supabasedemo.model

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val id: Int,
    val name: String
)