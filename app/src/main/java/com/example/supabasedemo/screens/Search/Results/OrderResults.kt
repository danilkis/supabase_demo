package com.example.supabasedemo.screens.Search.Results

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.OrderCard
import com.example.supabasedemo.screens.Search.ordersViewModel

@Composable
fun OrdersResults(query: String, navController: NavController, onResult: (Int) -> Unit) {

    val orders by ordersViewModel.orders.collectAsState(initial = listOf())
    val filteredThings = orders.filter { order ->
        order.created_at.contains(query, ignoreCase = true) ||
                order.deadline.contains(query, ignoreCase = true) ||
                order.status.toString().contains(query, ignoreCase = true) ||
                order.BillingId.toString().contains(query, ignoreCase = true) ||
                order.name.contains(query, ignoreCase = true) ||
                order.personId.toString().contains(query, ignoreCase = true)
    }
    LaunchedEffect(filteredThings.size) {
        onResult(filteredThings.size)
    }
    LazyColumn {
        items(filteredThings) { order ->
            OrderCard(
                order,
                navController // replace with actual NavController
            )
        }
    }
}
