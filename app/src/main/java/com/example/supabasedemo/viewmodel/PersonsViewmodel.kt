package com.example.supabasedemo.viewmodel

import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonsViewmodel : ViewModel() {
    var persons: Flow<MutableList<Persons>> = flow {
        val cont = getContacts()
        emit(cont)
    }

    var deleteComplete: Boolean = false
    suspend fun delete(personId: Int)
    {
        withContext(Dispatchers.Main) {
            deletePerson(personId)
        }
    }

    private suspend fun getContacts(): MutableList<Persons> {
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
                try
                {
                    var asyncClient = getAsyncClient()
                    asyncClient.postgrest["Persons"].delete(){
                        eq("id", personId)
                    }
                }
                catch (e: Exception)
                {
                    Log.e("SUPA", e.toString())
                }
            }
    }

    suspend fun insert(contact: Contacts, person: Persons)
    {
        Log.e("SUPA", "CALL PRIVATE")
        insertContact(contact, person)
    }
    private suspend fun insertContact(contact: Contacts, person: Persons)
    {
        withContext(Dispatchers.Default) {
            try
            {
                var asyncClient = getAsyncClient()
                var info = asyncClient.postgrest["Contacts"].select().decodeList<Contacts>().last()
                Log.e("SUPA", info.id.toString())
            }
            catch (e: Exception)
            {
                Log.e("SUPA", e.toString())
            }
        }
        }
}