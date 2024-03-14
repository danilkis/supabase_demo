package com.example.supabasedemo.viewmodel.Shelf

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.supabasedemo.model.Shelf.Shelf
import com.example.supabasedemo.supabase.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class ShelfViewmodel: ViewModel() {
    var shelves: Flow<List<Shelf>> = flow {
        val cont = getShelves()
        emit(cont)
    }

    private suspend fun getShelves(): List<Shelf> {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            withContext(Dispatchers.Main) {
                try {
                    val asyncClient = supaHelper.getAsyncClient()
                    return@withContext asyncClient.postgrest["shelf"].select().decodeList<Shelf>()
                } catch (e: Exception) {
                    Log.e("MVVM", e.toString())
                    return@withContext emptyList()
                }
            }
        }
    }
}