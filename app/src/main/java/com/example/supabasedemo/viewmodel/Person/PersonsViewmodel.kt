package com.example.supabasedemo.viewmodel.Person

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.supabasedemo.model.Persons.Contacts
import com.example.supabasedemo.model.Persons.Persons
import com.example.supabasedemo.supabase.supaHelper
import com.example.supabasedemo.supabase.supaHelper.Companion.getAsyncClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class PersonsViewmodel : ViewModel() {
    val asyncClient = getAsyncClient()

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
                return@withContext asyncClient.postgrest["Persons"].select().decodeList<Persons>().toMutableList()
            } catch (e: Exception) {
                Log.e("pers", e.toString())
                return@withContext emptyList<Persons>().toMutableList() //TODO: Поменять на простой лист
            }
        }
    }

    suspend fun deletePerson(personId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val info_person = asyncClient.postgrest["Persons"].select {
                    eq("id", personId)
                }.decodeSingle<Persons>()
                asyncClient.postgrest["Contacts"].delete {
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
                val newContact = asyncClient.postgrest["Contacts"].insert(
                    Contacts(
                        UUID.randomUUID().toString(),
                        contacts.phone,
                        contacts.telegram,
                        contacts.url,
                        supaHelper.userUUID
                    )
                ).decodeSingle<Contacts>()
                val new_person = Persons(
                    UUID.randomUUID().toString(),
                    person.Name,
                    person.Surname,
                    newContact.id,
                    supaHelper.userUUID
                )

                Log.e("Session stats: Session", supaHelper.client.gotrue.sessionManager.loadSession().toString())
                Log.e("Session stats: Sess val", supaHelper.client.gotrue.sessionStatus.value.toString())
                Log.e("Pers info", new_person.toString())

                Log.e("API Info", supaHelper.client.gotrue.currentSessionOrNull().toString())

                asyncClient.postgrest["Persons"].insert(new_person)
                reloadPersons()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }
}