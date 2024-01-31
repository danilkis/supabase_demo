package com.example.supabasedemo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.supabasedemo.model.Order_things
import com.example.supabasedemo.model.Orders
import com.example.supabasedemo.supa.supaHelper
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class OrderThingsViewmodel : ViewModel() {
    var thingId = 0
    var OrderThings: Flow<List<Orders>> = flow {
        val cont = getOrderThings()
        emit(cont)
    }

    private suspend fun getOrderThings(): List<Orders> {
        return withContext(Dispatchers.IO) {
            // Ensure SupabaseClient is initialized on the main thread
            val asyncClient = supaHelper.getAsyncClient()
            withContext(Dispatchers.Main) {
                try {
                    val orderThings = asyncClient.postgrest["Order_things"].select()
                    {
                        eq("ThingId", thingId)
                    }.decodeList<Order_things>()
                    // Get the Orders related to the OrderIds
                    val relatedOrders = mutableListOf<Orders>()
                    orderThings.forEach { orderThing ->
                        val order = asyncClient.postgrest["Orders"].select()
                        {
                            eq("id", orderThing.OrderId)
                        }.decodeSingle<Orders>()
                        if (order != null) {
                            relatedOrders.add(order)
                        }
                    }
                    relatedOrders
                } catch (e: Exception) {
                    Log.e("MVVM", e.toString())
                    return@withContext emptyList()
                }
            }
        }
    }

}