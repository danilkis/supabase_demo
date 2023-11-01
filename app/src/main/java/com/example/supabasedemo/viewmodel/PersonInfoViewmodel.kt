package com.example.supabasedemo.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject

class PersonInfoViewmodel(persons: Persons) : ViewModel() {
    val _contacts = MutableStateFlow<MutableList<Contacts>>(mutableListOf())
    var contact: StateFlow<MutableList<Contacts>> = _contacts
    init {
        CoroutineScope(Dispatchers.IO).launch {
            reloadPersons(persons)
        }
    }
    suspend fun getContacts(person: Persons): MutableList<Contacts> {
        return withContext(Dispatchers.Main) {
            try {
                Log.e("Supabase", person.toString())
                var asyncClient = supaHelper.getAsyncClient()
                val contact = asyncClient.postgrest["Contacts"]
                    .select()
                    {
                        eq("id", person.id)
                    }
                    .decodeList<Contacts>()
                return@withContext contact
            } catch (e: Exception) {
                return@withContext emptyList()
            }
        }.toMutableList()
    }

    suspend fun reloadPersons(person: Persons) {
        _contacts.emit(getContacts(person))
    }

}
class PersonInfoViewmodelFactory(val persons: Persons) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = PersonInfoViewmodel(persons) as T
}