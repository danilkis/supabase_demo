package com.example.supabasedemo.model

import kotlinx.serialization.Serializable

@Serializable
data class Order_things(
    val OrderId: Int,
    val ThingId: Int
)