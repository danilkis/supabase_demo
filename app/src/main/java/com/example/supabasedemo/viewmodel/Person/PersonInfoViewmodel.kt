package com.example.supabasedemo.viewmodel.Person

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.supabasedemo.model.Persons.Contacts
import com.example.supabasedemo.model.Persons.Persons
import com.example.supabasedemo.supabase.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PersonInfoViewmodel(persons: Persons) : ViewModel() {
    var contacts: Flow<Contacts> = flow {
        val cont = getContacts(persons)
        emit(cont)
    }

    suspend fun getContacts(person: Persons): Contacts {
        return try {
            Log.e("Supabase", person.toString())
            val asyncClient = supaHelper.getAsyncClient()
            val contact = asyncClient.postgrest["Contacts"]
                .select()
                {
                    eq("id", person.contactsId)
                }
                .decodeSingle<Contacts>()
            contact
        } catch (e: Exception) {
            Contacts("", "0", "0", "0", supaHelper.userUUID)
        }
    }
}

class PersonInfoViewmodelFactory(val persons: Persons) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = PersonInfoViewmodel(persons) as T
}