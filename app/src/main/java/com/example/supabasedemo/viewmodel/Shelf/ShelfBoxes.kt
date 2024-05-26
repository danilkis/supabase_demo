package com.example.supabasedemo.viewmodel.Shelf

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supabasedemo.model.Shelf.Shelf
import com.example.supabasedemo.model.Shelf.shelf_boxes
import com.example.supabasedemo.model.Things.Box
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

class ShelfBoxesViewmodel: ViewModel() {
    var shelves_boxes: Flow<List<shelf_boxes>> = flow {
        val cont = getShelves()
        emit(cont)
        delay(500)
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private suspend fun getShelves(): List<shelf_boxes> {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            withContext(Dispatchers.Main) {
                try {
                    val asyncClient = supaHelper.getAsyncClient()
                    val res =
                        asyncClient.postgrest["Shelf_boxes"].select().decodeList<shelf_boxes>()
                    return@withContext res
                } catch (e: Exception) {
                    Log.e("MVVM", e.toString())
                    return@withContext emptyList()
                }
            }
        }
    }

    suspend fun addBoxToShelf(shelf: Shelf, box: Box) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                val info = asyncClient.postgrest["Shelf"].select().decodeList<Shelf>()
                val new_contact =
                    shelf_boxes(
                        UUID.randomUUID().toString(),
                        shelf.id,
                        box.id,
                        supaHelper.userUUID
                    )
                asyncClient.postgrest["Shelf_boxes"].insert(
                    new_contact,
                    returning = Returning.MINIMAL
                )
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }
}