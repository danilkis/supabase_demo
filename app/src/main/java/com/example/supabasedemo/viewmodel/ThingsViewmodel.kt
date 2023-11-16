package com.example.supabasedemo.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.supabasedemo.model.Contacts
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.model.Things
import com.example.supabasedemo.model.Type
import com.example.supabasedemo.supa.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Returning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThingsViewmodel : ViewModel() {
    private val _things = MutableStateFlow<MutableList<Things>>(mutableListOf())

    var things: StateFlow<MutableList<Things>> = _things

    private val _types = MutableStateFlow<MutableList<Type>>(mutableListOf())
    var types: StateFlow<MutableList<Type>> = _types

    init {
        CoroutineScope(Dispatchers.IO).launch {
            reloadThings()
        }
    }

    var deleteComplete = mutableStateOf(false)
    suspend fun delete(thingId: Int) {
        withContext(Dispatchers.Main) {
            deleteThing(thingId)
            deleteComplete.value = true
        }
    }

    suspend fun getThings(): MutableList<Things> {
        return withContext(Dispatchers.Main) {
            try {
                Log.e("SUPA", "Getting data from supa")
                var asyncClient = supaHelper.getAsyncClient()
                return@withContext asyncClient.postgrest["Things"].select().decodeList<Things>()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
                return@withContext emptyList()
            }
        } as MutableList<Things>
    }

    private suspend fun deleteThing(thingId: Int) {
        withContext(Dispatchers.Main) {
            try {
                var asyncClient = supaHelper.getAsyncClient()
                asyncClient.postgrest["Things"].delete() {
                    eq("id", thingId)
                }
                reloadThings()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }

    suspend fun insertThing(thing: Things) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                var asyncClient = supaHelper.getAsyncClient()
                var info = asyncClient.postgrest["Things"].select().decodeList<Things>()
                val new_thing =
                    Things(info.last().id!! + 1, thing.name, thing.store, thing.amount, thing.type, thing.photoUrl)
                asyncClient.postgrest["Things"].insert(
                    new_thing,
                    returning = Returning.HEADERS_ONLY
                )
                reloadThings()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }

    }

    suspend fun getTypes(): MutableList<Type> {
        return withContext(Dispatchers.Main) {
            try {
                Log.e("SUPA", "Getting data from supa")
                var asyncClient = supaHelper.getAsyncClient()
                return@withContext asyncClient.postgrest["Thing_types"].select().decodeList<Type>()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
                return@withContext emptyList()
            }
        } as MutableList<Type>
    }

    suspend fun reloadThings() {
        _things.emit(getThings())
        _types.emit(getTypes())
        deleteComplete.value = false
    }
}