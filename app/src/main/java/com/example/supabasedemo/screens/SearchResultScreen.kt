package com.example.supabasedemo.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.supabasedemo.customelements.BoxCard
import com.example.supabasedemo.customelements.OrderCard
import com.example.supabasedemo.customelements.PersonCard
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.customelements.ThingCard
import com.example.supabasedemo.viewmodel.BoxViewmodel
import com.example.supabasedemo.viewmodel.OrderViewmodel
import com.example.supabasedemo.viewmodel.PersonsViewmodel
import com.example.supabasedemo.viewmodel.ThingsViewmodel
import kotlinx.coroutines.delay

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

val boxViewModel = BoxViewmodel()
val ordersViewModel = OrderViewmodel()
val thingsViewModel = ThingsViewmodel()
val personsViewModel = PersonsViewmodel()

@Composable
fun ThingsResults(query: String, onResult: (Int) -> Unit) {
    val types by thingsViewModel.types.collectAsState(initial = mutableListOf())
    val things by thingsViewModel.things.collectAsState()

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
                onClick = { /* define onClick action here */ },
                LongClickAction = { /* define LongClickAction here */ }
            )
        }
    }
}

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
                navController = navController // replace with actual NavController
            )
        }
    }
}

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
