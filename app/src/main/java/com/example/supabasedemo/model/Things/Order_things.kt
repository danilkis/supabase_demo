package com.example.supabasedemo.model.Things

import kotlinx.serialization.Serializable

@Serializable
data class Order_things(  //TODO: Удалить
    val OrderId: Int,
    val ThingId: Int
)