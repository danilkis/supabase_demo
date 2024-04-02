package com.example.supabasedemo.model.Persons

import androidx.compose.runtime.saveable.Saver
import com.example.supabasedemo.supabase.supaHelper
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Persons(
    val id: String,
    val Name: String,
    val Surname: String?,
    val contactsId: String,
    val user_id: String = supaHelper.userUUID
)
val HolderSaver = Saver<Persons, String>(
    save = { Json.encodeToString(it) },
    restore = { Json.decodeFromString(it) }
)