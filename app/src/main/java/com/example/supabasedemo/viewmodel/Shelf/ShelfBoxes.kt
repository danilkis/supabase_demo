package com.example.supabasedemo.viewmodel.Shelf

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.supabasedemo.model.Shelf.shelf_boxes
import com.example.supabasedemo.supabase.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class ShelfBoxesViewmodel: ViewModel() {
    var shelves_boxes: Flow<List<shelf_boxes>> = flow {
        val cont = getShelves()
        emit(cont)
    }

    private suspend fun getShelves(): List<shelf_boxes> {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            withContext(Dispatchers.Main) {
                try {
                    val asyncClient = supaHelper.getAsyncClient()
                    return@withContext asyncClient.postgrest["Shelf_boxes"].select().decodeList<shelf_boxes>()
                } catch (e: Exception) {
                    Log.e("MVVM", e.toString())
                    return@withContext emptyList()
                }
            }
        }
    }
}