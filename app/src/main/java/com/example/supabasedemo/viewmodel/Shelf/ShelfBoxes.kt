package com.example.supabasedemo.viewmodel.Shelf

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supabasedemo.model.Shelf.shelf_boxes
import com.example.supabasedemo.supabase.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class ShelfBoxesViewmodel: ViewModel() {
    var shelves_boxes: Flow<List<shelf_boxes>> = flow {
        val cont = getShelves()
        emit(cont)
        Log.e("ShelfBoxFlow", "REQ")
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
                    Log.i("Shelf_box", res.toString())
                    return@withContext res
                } catch (e: Exception) {
                    Log.e("MVVM", e.toString())
                    return@withContext emptyList()
                }
            }
        }
    }
}