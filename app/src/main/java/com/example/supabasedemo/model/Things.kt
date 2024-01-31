package com.example.supabasedemo.model

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.res.stringResource
import com.example.supabasedemo.R
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
    @SuppressLint("DiscouragedApi")
    fun getTypeName(types: MutableList<Type>, ctx: Context): Int {
        val stringKey = types.find { it.id == type }?.Name ?: "nothing"

        var res = ctx.getResources().getIdentifier(stringKey, "string", ctx.packageName)
        if (res == 0)
        {
            res = R.string.yes
        }
        return res
    }
}
