package com.example.supabasedemo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.supa.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PersonsViewmodel : ViewModel() {
    var persons: Flow<List<Persons>> = flow {
        if(supaHelper.isInitDone)
        {
            emit(supaHelper.client.postgrest["Persons"].select().decodeList<Persons>())
        }
        else
        {
            emit(emptyList())
        }
    }
}