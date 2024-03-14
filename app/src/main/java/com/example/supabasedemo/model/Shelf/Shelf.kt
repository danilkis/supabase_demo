package com.example.supabasedemo.model.Shelf

import kotlinx.serialization.Serializable

/** Модель серериализации стелажей
 * @param id ID стелажа
 * @param name наименование стелажа
 * @param available_levels Количество полок
 * @param room место нахождения стелажа
 */
@Serializable
data class Shelf(
    val id: Int,
    val name: String,
    val available_levels: Int,
    val room: String
)