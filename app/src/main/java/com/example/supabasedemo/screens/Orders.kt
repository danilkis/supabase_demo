package com.example.supabasedemo.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.OrderCard
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.viewmodel.OrderViewmodel

@Composable
fun OrdersMainScreen(navController: NavController, viewModel: OrderViewmodel = viewModel())
{
    val orders by viewModel.orders.collectAsState(initial = listOf())
    if (orders.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBarCustom()
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(60.dp))
            }
        }
    }
    else
    {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBarCustom()
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                OrderCard(Order = orders[0], navController = navController)
            }
        }
    }
}
