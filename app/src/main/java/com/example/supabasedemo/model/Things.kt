package com.example.supabasedemo.model

import androidx.compose.runtime.collectAsState
import com.example.supabasedemo.viewmodel.ThingsViewmodel
import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable

@Serializable
data class Things(
    val id: Int,
    val name: String,
    val store: String?,
    val amount: Int,
    val type: Int,
    val photoUrl: String?
) {
    fun getTypeName(types: MutableList<Type>): String {
        return types.find { it.id == type }?.Name ?: "nothing"
    }
}
