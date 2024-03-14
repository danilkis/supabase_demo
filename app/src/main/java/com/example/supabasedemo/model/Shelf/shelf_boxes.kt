package com.example.supabasedemo.model.Shelf

import kotlinx.serialization.Serializable

/** Модель с рериализацией для стыкоывочной таблицы коробок и стелажей
 * @param id ID записи
 * @param ShelfID ID Стелажа
 * @param payment_recieved ID коробки
 */
@Serializable
data class shelf_boxes(
    val id: Int,
    val ShelfID: Int,
    val BoxID: Int,
)