package com.example.supabasedemo.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable


/** Модель с рериализацией для таблицы с чеками
 * @param id ID чека
 * @param created_at дата создания
 * @param payment_recieved дата получения платежа
 * @param pay оплдата
 * @param status статус опллаты
 */
@Serializable
data class Billing(
    val id: Int,
    val created_at: DateTimePeriod,
    val payment_recieved: DateTimePeriod?, //TODO: Поставить обычный timestamp
    val pay: Float,
    val status: Int
)