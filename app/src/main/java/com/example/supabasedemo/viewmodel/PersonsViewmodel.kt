package com.example.supabasedemo.viewmodel

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
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            val asyncClient = null
            withContext(Dispatchers.Main) {
                try {
                    var asyncClient = getAsyncClient()
                    return@withContext asyncClient.postgrest["Persons"].select().decodeList<Persons>()
                } catch (e: Exception) {
                    return@withContext emptyList()
                }
            }
        } as MutableList<Persons>
    }
    private suspend fun deletePerson(personId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            val asyncClient = null
            withContext(Dispatchers.Main) {
                try
                {
                    var asyncClient = getAsyncClient()
                    asyncClient.postgrest["Persons"].delete(){
                        eq("id", personId)
                    }
                    return@withContext true
                }
                catch (e: Exception)
                {
                    return@withContext false
                }
            }
        }
    }
}