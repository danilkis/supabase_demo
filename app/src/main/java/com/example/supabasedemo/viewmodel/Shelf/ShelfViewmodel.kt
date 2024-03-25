package com.example.supabasedemo.viewmodel.Shelf

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supabasedemo.model.Shelf.Shelf
import com.example.supabasedemo.supabase.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Returning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShelfViewmodel: ViewModel() {
    val shelves: Flow<List<Shelf>> = flow {
        while (true) {
            delay(500)
            val cont = getShelves()
            emit(cont)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private suspend fun getShelves(): List<Shelf> {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            withContext(Dispatchers.Main) {
                try {
                    val asyncClient = supaHelper.getAsyncClient()
                    return@withContext asyncClient.postgrest["Shelf"].select().decodeList<Shelf>()
                } catch (e: Exception) {
                    Log.e("MVVM", e.toString())
                    return@withContext emptyList()
                }
            }
        }
    }

    suspend fun insertShelf(shelf: Shelf) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                val info = asyncClient.postgrest["Shelf"].select().decodeList<Shelf>()
                val new_contact =
                    Shelf(
                        info.last().id + 1,
                        shelf.name,
                        shelf.available_levels,
                        shelf.room,
                        shelf.floor
                    )
                asyncClient.postgrest["Shelf"].insert(
                    new_contact,
                    returning = Returning.MINIMAL
                )
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }

    }
}