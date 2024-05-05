package com.example.supabasedemo.model.Things

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Модель серериализацией для коробки
 * @param id ID коробки
 * @param name название коробки
 * @param barcode штрихкод в строковом представлении
 */
@Serializable
data class Box(
    val id: String,
    val name: String,
    val user_id: String
) {
    fun getBoxName(types: MutableList<Box>): String {
        return types.find { it.id == id }?.name ?: "Nothing"
    }
}

val HolderSaverBox = Saver<Box, String>(
    save = { Json.encodeToString(it) },
    restore = { Json.decodeFromString(it) }
)