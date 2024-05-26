package com.example.supabasedemo.model.Shelf

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Модель серериализации стелажей
 * @param id ID стелажа
 * @param name наименование стелажа
 * @param available_levels Количество полок
 * @param room место нахождения стелажа
 */
@Serializable
data class Shelf(
    val id: String,
    val name: String,
    val available_levels: Int?,
    val room: String,
    val floor: Int,
    val user_id: String
)

val HolderSaverShelf = Saver<Shelf, String>(
    save = { Json.encodeToString(it) },
    restore = { Json.decodeFromString(it) }
)