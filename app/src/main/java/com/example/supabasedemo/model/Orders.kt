package com.example.supabasedemo.model

import kotlinx.serialization.Serializable

/** Модель заказов (Orders)
 * @param id ID заказа
 * @param created_at дата создания
 * @param deadline срок закрытия заказа
 * @param billingId ID для ссылки на чек
 * @param status статус заказа
 */
@Serializable
data class Orders(
    val id: Int,
    val created_at: String,
    val deadline: String,
    val status: Int,
    val BillingId: Int,
    val name: String,
    val personId: Int
)