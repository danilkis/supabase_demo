package com.example.supabasedemo.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.supabasedemo.client
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import com.example.supabasedemo.model.Contacts
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.supa.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject

class PersonInfoViewmodel(val person: Persons) : ViewModel() {
    var contacts: Flow<Contacts> = flow {
        val cont = getContacts()
        emit(cont)
    }
    private suspend fun getContacts(): Contacts {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            val asyncClient = null
            withContext(Dispatchers.Main) {
                try
                {
                    var asyncClient = supaHelper.getAsyncClient()

                    val contact = asyncClient.postgrest["Contacts"]
                        .select()
                        {
                            eq("id", person.contactsId)
                        }
                        .decodeSingle<Contacts>()
                    Log.e("SUPA", contact.toString())
                    return@withContext contact
                }
                catch (e: Exception)
                {
                    Log.e("SUPA", e.toString())
                    return@withContext Contacts(0, "0", "0", "0")
                }
            }
        }
    }
}
class PersonInfoViewmodelFactory(private val person: Persons) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = PersonInfoViewmodel(person) as T
}