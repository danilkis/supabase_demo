package com.example.supabasedemo.screens.Search.Results

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.ShelfCard
import com.example.supabasedemo.screens.Search.shelfViewmodel

@Composable
fun ShelfResults(query: String, navController: NavController, onResult: (Int) -> Unit) {

    val shelf by shelfViewmodel.shelves.collectAsState(initial = listOf())
    val filteredThings = shelf.filter { it ->
        it.name.contains(query) || it.room.contains(query)
    }
    LaunchedEffect(filteredThings.size) {
        onResult(filteredThings.size)
    }
    LazyColumn {
        items(filteredThings) { it ->
            ShelfCard(
                it,
                navController,
                {},
                { navController.navigate("shelf/${it.id}") }
            )
        }
    }
}
