package com.example.supabasedemo.model

import kotlinx.serialization.Serializable

@Serializable
data class Things(
    val id: Int,
    val name: String,
    val store: String?,
    val amount: Int,
    val type: Int,
    val photoUrl: String?,
    val boxId: Int
) {
    fun getTypeName(types: MutableList<Type>): String {
        return types.find { it.id == type }?.Name ?: "nothing"
    }
}
