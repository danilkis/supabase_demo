package com.example.supabasedemo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.supabasedemo.model.Orders
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.model.Status
import com.example.supabasedemo.supa.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class OrderViewmodel: ViewModel() {
    var orders: Flow<List<Orders>> = flow {
        val cont = getOrders()
        emit(cont)
    }

    val statuses: Flow<List<Status>> =  flow {
        val sts = getStatus()
        emit(sts)
    }


    private suspend fun getOrders(): List<Orders> {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            val asyncClient = null
            withContext(Dispatchers.Main) {
                try
                {
                    var asyncClient = supaHelper.getAsyncClient()
                    return@withContext asyncClient.postgrest["Orders"].select().decodeList<Orders>()
                }
                catch (e: Exception)
                {
                    Log.e("MVVM", e.toString())
                    return@withContext emptyList()
                }
            }
        }
    }
    private suspend fun getStatus(): List<Status> {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            val asyncClient = null
            withContext(Dispatchers.Main) {
                try
                {
                    var asyncClient = supaHelper.getAsyncClient()
                    return@withContext asyncClient.postgrest["status"].select().decodeList<Status>()
                }
                catch (e: Exception)
                {
                    Log.e("MVVM", e.toString())
                    return@withContext emptyList()
                }
            }
        }
    }
}