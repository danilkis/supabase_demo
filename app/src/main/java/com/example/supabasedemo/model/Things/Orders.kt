package com.example.supabasedemo.model.Things

import kotlinx.serialization.Serializable

/** Модель серериализацией для заказов
 * @param id ID заказа
 * @param created_at дата создания
 * @param deadline срок закрытия заказа
 * @param BillingId ID для ссылки на чек
 * @param status статус заказа
 */
@Serializable
data class Orders( //TODO: Удалить
    val id: Int,
    val created_at: String,
    val deadline: String,
    val status: Int,
    val BillingId: Int,
    val name: String,
    val personId: Int
)