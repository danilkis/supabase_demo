package com.example.supabasedemo.viewmodel

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
    var persons: Flow<List<Persons>> = flow {
        val cont = getContacts()
        emit(cont)
    }

    private suspend fun getContacts(): List<Persons> {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            val asyncClient = null
            withContext(Dispatchers.Main) {
                var asyncClient = getAsyncClient()
                return@withContext asyncClient.postgrest["Persons"].select().decodeList<Persons>()
            }
        }
    }
}