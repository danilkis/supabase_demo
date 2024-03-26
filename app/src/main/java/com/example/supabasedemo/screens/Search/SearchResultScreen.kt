package com.example.supabasedemo.screens.Search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.example.supabasedemo.screens.Search.Results.BoxResults
import com.example.supabasedemo.screens.Search.Results.OrdersResults
import com.example.supabasedemo.screens.Search.Results.PersonsResults
import com.example.supabasedemo.screens.Search.Results.ThingsResults
import com.example.supabasedemo.viewmodel.Order.OrderViewmodel
import com.example.supabasedemo.viewmodel.Person.PersonsViewmodel
import com.example.supabasedemo.viewmodel.Things.BoxViewmodel
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import kotlinx.coroutines.delay

val boxViewModel = BoxViewmodel()
val ordersViewModel = OrderViewmodel()
val thingsViewModel = ThingsViewmodel()
val personsViewModel = PersonsViewmodel()

@Composable
fun SearchResultScreen(query: String, navController: NavController) {
    var thingsCount by remember { mutableStateOf(0) }
    var personsCount by remember { mutableStateOf(0) }
    var ordersCount by remember { mutableStateOf(0) }
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
        Scaffold(topBar = { SearchBarCustom(navController) })
        { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding(), start = 6.dp, end = 6.dp),
            ) {
                PersonsResults(query = query, navController = navController) { count ->
                    personsCount = count
                }
                if (personsCount > 0) {
                    Text(
                        stringResource(id = R.string.persons),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                OrdersResults(query, navController) { count -> ordersCount = count }
                if (ordersCount > 0) {
                    Text(
                        stringResource(id = R.string.orders),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                ThingsResults(query = query) { count -> thingsCount = count }
                if (thingsCount > 0) {
                    Text(
                        stringResource(id = R.string.things),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                BoxResults(query = query, navController) { count -> boxesCount = count }
                if (boxesCount > 0) {
                    Text(
                        stringResource(id = R.string.boxes),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }

                if (personsCount + ordersCount + thingsCount + boxesCount <= 0) {
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





