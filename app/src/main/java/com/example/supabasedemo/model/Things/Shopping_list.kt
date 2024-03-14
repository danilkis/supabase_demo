package com.example.supabasedemo.model.Things

import kotlinx.serialization.Serializable

@Serializable
data class Shopping_list(
    val id: Int,
    val thingId: Int,
    val status: Int
)