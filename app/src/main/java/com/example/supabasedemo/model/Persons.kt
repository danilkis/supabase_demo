package com.example.supabasedemo.model

import kotlinx.serialization.Serializable

/** Модель для людей (таблица persons)
 * @param id ID человека
 * @param Name Имя
 * @param Surname Фамилия
 * @param contactsId ID контактов
 */
@Serializable
data class Persons(
    val id: Int,
    val Name: String,
    val Surname: String?,
    val contactsId: Int
)