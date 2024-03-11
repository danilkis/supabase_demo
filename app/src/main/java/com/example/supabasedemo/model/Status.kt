package com.example.supabasedemo.model

import kotlinx.serialization.Serializable


/** Модель таблицы статусов (status)
 * @param id ID статуса
 * @param name Статус
 */
@Serializable
data class Status(
    val id: Int,
    val name: String
)