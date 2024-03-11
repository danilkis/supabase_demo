package com.example.supabasedemo.model

import kotlinx.serialization.Serializable

/** Модель таблицы типов (types)
 * @param id ID типа
 * @param name тип (R.string.. для локализации)
 */
@Serializable
data class Type(
    val id: Int,
    val Name: String
)