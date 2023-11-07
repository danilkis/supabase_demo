package com.example.supabasedemo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.jan.supabase.postgrest.postgrest
import com.example.supabasedemo.model.Contacts
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.supa.supaHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class PersonInfoViewmodel(persons: Persons) : ViewModel() {
    var contacts: Flow<Contacts> = flow {
        val cont = getContacts(persons)
        emit(cont)
    }
    suspend fun getContacts(person: Persons): Contacts {
            try {
                Log.e("Supabase", person.toString())
                val asyncClient = supaHelper.getAsyncClient()
                val contact = asyncClient.postgrest["Contacts"]
                    .select()
                    {
                        eq("id", person.contactsId)
                    }
                    .decodeSingle<Contacts>()
                return contact
            } catch (e: Exception) {
                return Contacts(0, "0", "0", "0")
            }
    }
}
class PersonInfoViewmodelFactory(val persons: Persons) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = PersonInfoViewmodel(persons) as T
}