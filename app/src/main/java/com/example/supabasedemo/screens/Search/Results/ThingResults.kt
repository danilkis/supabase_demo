package com.example.supabasedemo.screens.Search.Results

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.ThingCard
import com.example.supabasedemo.customelements.ThingSheet
import com.example.supabasedemo.screens.Search.thingsViewModel

@Composable
fun ThingsResults(navController: NavController, query: String, onResult: (Int) -> Unit) {
    val types by thingsViewModel.types.collectAsState(initial = mutableListOf())
    val things by thingsViewModel.things.collectAsState(initial = mutableListOf())
    val openSheet = remember { mutableStateOf(false) }
    val thingID = remember { mutableStateOf("") }
    val filteredThings = things.filter { thing ->
        thing.name.contains(query, ignoreCase = true) ||
                thing.store?.contains(query, ignoreCase = true) == true ||
                thing.amount.toString().contains(query, ignoreCase = true) ||
                thing.type.toString().contains(query, ignoreCase = true) ||
                thing.photoUrl?.contains(query, ignoreCase = true) == true ||
                thing.boxId.toString().contains(query, ignoreCase = true)
    }
    LaunchedEffect(filteredThings.size) {
        onResult(filteredThings.size)
    }
    LazyColumn {
        items(filteredThings) { thing ->
            ThingCard(
                thing = thing,
                type = types, // replace with actual types
                onClick = { openSheet.value = true; thingID.value = thing.id },
                LongClickAction = { /* define LongClickAction here */ }
            )
        }
    }
    if (openSheet.value) {
        things.find { it.id == thingID.value }?.let {
            ThingSheet(
                thing = it,
                types,
                { openSheet.value = false },
                navController
            )
        }
    }
}