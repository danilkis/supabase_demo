package com.example.supabasedemo.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supabasedemo.client
import com.example.supabasedemo.model.Contacts
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.supa.supaHelper
import com.example.supabasedemo.supa.supaHelper.Companion.getAsyncClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Returning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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
    suspend fun delete(personId: Int) {
        withContext(Dispatchers.Main) {
            deletePerson(personId)
            deleteComplete.value = true
        }
    }

    suspend fun getContacts(): MutableList<Persons> {
        return withContext(Dispatchers.Main) {
            try {
                var asyncClient = getAsyncClient()
                return@withContext asyncClient.postgrest["Persons"].select().decodeList<Persons>()
            } catch (e: Exception) {
                return@withContext emptyList()
            }
        }.toMutableList()
    }

    private suspend fun deletePerson(personId: Int) {
        withContext(Dispatchers.Main) {
            try {
                var asyncClient = getAsyncClient()
                asyncClient.postgrest["Persons"].delete() {
                    eq("id", personId)
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
                var asyncClient = getAsyncClient()
                var info = asyncClient.postgrest["Contacts"].select().decodeList<Contacts>()
                val new_contact =
                    Contacts(info.last().id!! + 1, contacts.phone, contacts.telegram, contacts.url)
                asyncClient.postgrest["Contacts"].insert(
                    new_contact,
                    returning = Returning.HEADERS_ONLY
                )
                var info_person = asyncClient.postgrest["Persons"].select().decodeList<Persons>()
                val new_person = Persons(
                    info_person.last().id!! + 1,
                    person.Name,
                    person.Surname,
                    info.last().id!! + 1
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