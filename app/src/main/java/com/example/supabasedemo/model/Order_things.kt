package com.example.supabasedemo.model

import kotlinx.serialization.Serializable
/** Модель смежной таблицы Order <-> Things
 * @param OrderId ID Заказа
 * @param ThingId ID Вещи
 */
@Serializable
data class Order_things(
    val OrderId: Int,
    val ThingId: Int
)