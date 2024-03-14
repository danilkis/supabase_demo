package com.example.supabasedemo.screens.Shelf

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.ShelfCard
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.viewmodel.Shelf.ShelfViewmodel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrdersMainScreen(navController: NavController, viewModel: ShelfViewmodel = viewModel()) {
    val openDialog = remember { mutableStateOf(false) }
    Scaffold(topBar = { SearchBarCustom(navController) }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                openDialog.value = true
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add icon")
        }
    })
    { paddingValues ->
        val shelf by viewModel.shelves.collectAsState(initial = listOf())
        if (shelf.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(60.dp))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding(), start = 6.dp, end = 6.dp)
            )
            {
                items(shelf) {
                    ShelfCard(it, navController)
                }
            }
        }
    }
}