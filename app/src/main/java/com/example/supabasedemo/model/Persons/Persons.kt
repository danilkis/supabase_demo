package com.example.supabasedemo.model.Persons

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Persons(
    val id: Int,
    val Name: String,
    val Surname: String?,
    val contactsId: Int
)
val HolderSaver = Saver<Persons, String>(
    save = { Json.encodeToString(it) },
    restore = { Json.decodeFromString(it) }
)