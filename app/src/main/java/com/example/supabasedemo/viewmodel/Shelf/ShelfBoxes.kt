package com.example.supabasedemo.viewmodel.Shelf

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supabasedemo.model.Shelf.Shelf
import com.example.supabasedemo.model.Shelf.shelf_boxes
import com.example.supabasedemo.model.Things.Box
import com.example.supabasedemo.supabase.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Returning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ShelfBoxesViewmodel: ViewModel() {
    private val _shelves_boxes = MutableStateFlow<List<shelf_boxes>>(emptyList())
    val shelves_boxes: StateFlow<List<shelf_boxes>> get() = _shelves_boxes

    var deleteComplete = mutableStateOf(false)

    init {
        viewModelScope.launch {
            _shelves_boxes.value = getShelves()
        }
    }

    private suspend fun getShelves(): List<shelf_boxes> {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            withContext(Dispatchers.Main) {
                try {
                    val asyncClient = supaHelper.getAsyncClient()
                    val res =
                        asyncClient.postgrest["Shelf_boxes"].select().decodeList<shelf_boxes>()
                    Log.i("ShelfBoxesBackend", res.toString())
                    return@withContext res
                } catch (e: Exception) {
                    Log.e("MVVM", e.toString())
                    return@withContext emptyList()
                }
            }
        }
    }

    suspend fun addBoxToShelf(shelf: Shelf, box: Box) {
        viewModelScope.launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                val new_contact = shelf_boxes(
                    UUID.randomUUID().toString(),
                    shelf.id,
                    box.id,
                    supaHelper.userUUID
                )
                asyncClient.postgrest["Shelf_boxes"].insert(
                    new_contact,
                    returning = Returning.MINIMAL
                )
                _shelves_boxes.value = getShelves()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }

    suspend fun unlinkBoxFromShelf(shelf: Shelf, box: Box) {
        viewModelScope.launch {
            try {
                val asyncClient = supaHelper.getAsyncClient()
                asyncClient.postgrest["Shelf_boxes"].delete {
                    eq("ShelfID", shelf.id)
                    eq("BoxID", box.id)
                }
                _shelves_boxes.value = getShelves()
            } catch (e: Exception) {
                Log.e("SUPA", e.toString())
            }
        }
    }
}