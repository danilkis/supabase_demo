package com.example.supabasedemo.model.Shelf

import kotlinx.serialization.Serializable

/** Модель с рериализацией для стыкоывочной таблицы коробок и стелажей
 * @param id ID записи
 * @param ShelfID ID Стелажа
 * @param BoxID ID коробки
 */
@Serializable
data class shelf_boxes(
    val id: String,
    val ShelfID: String,
    val BoxID: String,
    val user_id: String
)