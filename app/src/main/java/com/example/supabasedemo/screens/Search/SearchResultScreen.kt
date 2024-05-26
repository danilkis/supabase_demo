package com.example.supabasedemo.screens.Search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.customelements.ToggleHeading
import com.example.supabasedemo.screens.Search.Results.BoxResults
import com.example.supabasedemo.screens.Search.Results.PersonsResults
import com.example.supabasedemo.screens.Search.Results.ShelfResults
import com.example.supabasedemo.screens.Search.Results.ThingsResults
import com.example.supabasedemo.viewmodel.Person.PersonsViewmodel
import com.example.supabasedemo.viewmodel.Shelf.ShelfViewmodel
import com.example.supabasedemo.viewmodel.Things.BoxViewmodel
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import kotlinx.coroutines.delay

val boxViewModel = BoxViewmodel()
val shelfViewmodel = ShelfViewmodel()
val thingsViewModel = ThingsViewmodel()
val personsViewModel = PersonsViewmodel()

@Composable
fun SearchResultScreen(query: String, navController: NavController) {
    var thingsCount by remember { mutableStateOf(0) }
    var personsCount by remember { mutableStateOf(0) }
    var shelfCount by remember { mutableStateOf(0) }
    var boxesCount by remember { mutableStateOf(0) }
    var showLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = query) {
        delay(200L)
        showLoading = false
    }

    if (showLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(topBar = { SearchBarCustom(navController) }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding(), start = 10.dp, end = 10.dp),
            ) {
                val totalResults = personsCount + shelfCount + thingsCount + boxesCount
                Text(
                    text = "$totalResults найдено",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp)
                )

                ToggleHeading(
                    heading = stringResource(id = R.string.persons),
                    expandByDefault = true,
                    Content = {
                        PersonsResults(query = query, navController = navController) { count ->
                            personsCount = count
                        }
                    })

                ToggleHeading(
                    heading = stringResource(id = R.string.shelves),
                    expandByDefault = true,
                    Content = {
                        ShelfResults(query, navController) { count -> shelfCount = count }
                    }
                )

                ToggleHeading(
                    heading = stringResource(id = R.string.things),
                    expandByDefault = true,
                    Content = {
                        ThingsResults(navController, query = query) { count -> thingsCount = count }
                    }
                )

                ToggleHeading(
                    heading = stringResource(id = R.string.boxes),
                    expandByDefault = true,
                    Content = {
                        BoxResults(query = query, navController) { count -> boxesCount = count }
                    }
                )

                if (totalResults <= 0) {
                    Text(
                        stringResource(id = R.string.nothing_found),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
    }
    // Handle back button press
    BackHandler {
        navController.navigate("mainScreen")
    }
}





