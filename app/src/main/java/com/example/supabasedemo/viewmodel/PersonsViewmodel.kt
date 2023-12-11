package com.example.supabasedemo.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.supabasedemo.model.Contacts
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.supa.supaHelper.Companion.getAsyncClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Returning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonsViewmodel : ViewModel() {

    val _persons = MutableStateFlow<MutableList<Persons>>(mutableListOf())

    var newPersons: StateFlow<MutableList<Persons>> = _persons

    init {
        CoroutineScope(Dispatchers.IO).launch {
            reloadPersons()
        }
    }

    var deleteComplete = mutableStateOf(false)

    suspend fun getContacts(): MutableList<Persons> {

        return withContext(Dispatchers.Main) {
            try {
                deleteComplete.value = false
                val asyncClient = getAsyncClient()
                return@withContext asyncClient.postgrest["Persons"].select().decodeList<Persons>()
            } catch (e: Exception) {
                return@withContext emptyList()
            }
        } as MutableList<Persons>
    }

    suspend fun deletePerson(personId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = getAsyncClient()
                val info_person = asyncClient.postgrest["Persons"].select() {
                    eq("id", personId)
                }.decodeSingle<Persons>()
                asyncClient.postgrest["Contacts"].delete() {
                    eq("id", info_person.contactsId)
                }
                reloadPersons()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }

    suspend fun reloadPersons() {
        _persons.emit(getContacts())
        deleteComplete.value = false
    }

    suspend fun insertContact(contacts: Contacts, person: Persons) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = getAsyncClient()
                val info = asyncClient.postgrest["Contacts"].select().decodeList<Contacts>()
                val new_contact =
                    Contacts(info.last().id + 1, contacts.phone, contacts.telegram, contacts.url)
                asyncClient.postgrest["Contacts"].insert(
                    new_contact,
                    returning = Returning.HEADERS_ONLY
                )
                val info_person = asyncClient.postgrest["Persons"].select().decodeList<Persons>()
                val new_person = Persons(
                    info_person.last().id + 1,
                    person.Name,
                    person.Surname,
                    info.last().id + 1
                )
                asyncClient.postgrest["Persons"].insert(
                    new_person,
                    returning = Returning.HEADERS_ONLY
                )
                reloadPersons()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }

    }
}