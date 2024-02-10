package com.example.supabasedemo.screens.Search.Results

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.BoxCard
import com.example.supabasedemo.screens.Search.boxViewModel

@Composable
fun BoxResults(query: String, navController: NavController, onResult: (Int) -> Unit) {
    val boxes by boxViewModel.boxes.collectAsState()
    val filteredThings = boxes.filter { box ->
        box.name.contains(query, ignoreCase = true) ||
                box.barcode?.contains(query, ignoreCase = true) == true
    }
    LaunchedEffect(filteredThings.size) {
        onResult(filteredThings.size)
    }
    LazyColumn {
        items(filteredThings) { box ->
            BoxCard(
                box = box,
                navController = navController // replace with actual NavController
            )
        }
    }
}
