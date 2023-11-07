package com.example.supabasedemo.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable

/** Модель серериализацией для заказов
 * @param id ID заказа
 * @param created_at дата создания
 * @param deadline срок закрытия заказа
 * @param billingId ID для ссылки на чек
 * @param status статус заказа
 */
@Serializable
data class Orders(
    val id: Int,
    val created_at: Long,
    val deadline: Long, //TODO: Поставить обычный timestamp
    val billingId: Int,
    val status: Int
)