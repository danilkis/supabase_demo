package com.example.supabasedemo.model.Things

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.saveable.Saver
import com.example.supabasedemo.R
import com.example.supabasedemo.model.Persons.Persons
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
    @SuppressLint("DiscouragedApi")
    fun getTypeName(types: MutableList<Type>, ctx: Context): Int {
        val stringKey = types.find { it.id == type }?.Name ?: "nothing"

        var res = ctx.resources.getIdentifier(stringKey, "string", ctx.packageName)
        if (res == 0) {
            res = R.string.yes
        }
        return res
    }
}
val HolderSaverThings = Saver<Persons, String>(
    save = { Json.encodeToString(it) },
    restore = { Json.decodeFromString(it) }
)