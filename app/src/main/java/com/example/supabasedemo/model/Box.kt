package com.example.supabasedemo.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable
/** Модель серериализацией для коробки
 * @param id ID коробки
 * @param name название коробки
 * @param barcode штрихкод в строковом представлении
 */
@Serializable
data class Box(
    val id: Int,
    val name: String,
    val barcode: String?
)