package com.example.supabasedemo.screens.Search.Results

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.PersonCard
import com.example.supabasedemo.screens.Search.personsViewModel

@Composable
fun PersonsResults(query: String, navController: NavController, onResult: (Int) -> Unit) {
    val persons by personsViewModel.newPersons.collectAsState()

    val filteredThings = persons.filter { person ->
        person.Name.contains(query, ignoreCase = true) ||
                person.Surname?.contains(query, ignoreCase = true) == true ||
                person.contactsId.toString().contains(query, ignoreCase = true)
    }
    LaunchedEffect(filteredThings.size) {
        onResult(filteredThings.size)
    }
    LazyColumn {
        items(filteredThings) { person ->
            PersonCard(
                person,
                navController = navController, // replace with actual NavController
                { navController.navigate("person/${person.id}") }
            )
        }
    }
}