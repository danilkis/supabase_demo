package com.example.supabasedemo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Orders
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.viewmodel.OrderViewmodel
import com.example.supabasedemo.viewmodel.PersonsViewmodel

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
            OrderColumn(orders)
        }
    }
}

@Composable
fun OrderColumn(orders: List<Orders>)
{
    Column()
    {
        for (order in orders)
        {
            OrdersCard(order)
        }
    }
}

@Composable
fun OrdersCard(Order: Orders) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Spacer(modifier = Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
            ) {
                Text(
                    text = Order.deadline.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}