package com.example.supabasedemo.viewmodel.Shelf

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
import java.util.UUID

@Suppress("LABEL_NAME_CLASH")
class ShelfViewmodel: ViewModel() {
    val shelves: Flow<List<Shelf>> = flow {
        while (true) {
            delay(500)
            val cont = getShelves()
            emit(cont)

            deleteComplete.value = false
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000L), emptyList())

    var deleteComplete = mutableStateOf(false)
    private suspend fun getShelves(): List<Shelf> {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            withContext(Dispatchers.Main) {
                try {
                    val asyncClient = supaHelper.getAsyncClient()
                    val res = asyncClient.postgrest["Shelf"].select().decodeList<Shelf>()
                    return@withContext res
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
                        UUID.randomUUID().toString(),
                        shelf.name,
                        shelf.available_levels,
                        shelf.room,
                        shelf.floor,
                        supaHelper.userUUID
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

    suspend fun deleteShelf(shelfId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                asyncClient.postgrest["Shelf"].delete {
                    eq("id", shelfId)
                }
                deleteComplete.value = true
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }

    suspend fun updateShelf(shelf: Shelf) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()

                asyncClient.postgrest["Shelf"].update(shelf)
                {
                    eq("id", shelf.id)
                }
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }
}