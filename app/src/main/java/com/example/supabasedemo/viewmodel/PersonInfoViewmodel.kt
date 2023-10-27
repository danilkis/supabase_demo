package com.example.supabasedemo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.supa.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PersonInfoViewmodel : ViewModel() {
    var persons: Flow<List<Persons>> = flow {
        val cont = getContacts()
        emit(cont)
    }

    private suspend fun getContacts(): List<Persons> {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            val asyncClient = null
            withContext(Dispatchers.Main) {
                try
                {
                    var asyncClient = supaHelper.getAsyncClient()
                    return@withContext asyncClient.postgrest["Persons"].select().decodeList<Persons>()
                }
                catch (e: Exception)
                {
                    return@withContext emptyList()
                }
            }
        }
    }
}