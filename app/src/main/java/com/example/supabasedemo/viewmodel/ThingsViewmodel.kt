package com.example.supabasedemo.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.model.Things
import com.example.supabasedemo.supa.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThingsViewmodel : ViewModel() {
    private val _things = MutableStateFlow<MutableList<Things>>(mutableListOf())

    var things: StateFlow<MutableList<Things>> = _things

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

    suspend fun reloadThings() {
        _things.emit(getThings())
        deleteComplete.value = false
    }
}