package com.example.supabasedemo.model

import android.annotation.SuppressLint
import android.content.Context
import com.example.supabasedemo.R
import kotlinx.serialization.Serializable


/** Модель таблицы для вещей (Things)
 * @param id ID вещи
 * @param name Название вещи
 * @param store Ссылка на магазин
 * @param amount Количество
 * @param type Тип вещи -> [Type]
 * @param photoUrl Ссылка на фото в S3 supabase
 * @param boxId ID коробки
 * getTypeName(types: MutableList<Type>, ctx: Context) - возращает название типа в правильной локализации
 */

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
