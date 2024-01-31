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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.PersonCard
import com.example.supabasedemo.customelements.ToggleHeading
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Box
import com.example.supabasedemo.model.Contacts
import com.example.supabasedemo.model.ExpandShapeState
import com.example.supabasedemo.model.Orders
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.viewmodel.OrderViewmodel
import com.example.supabasedemo.viewmodel.PersonInfoViewmodel
import com.example.supabasedemo.viewmodel.PersonInfoViewmodelFactory
import com.example.supabasedemo.viewmodel.PersonsViewmodel
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.text.DateFormat
import java.util.Locale

@Composable
fun OrderInfoScreen(order: Orders, navController: NavController) {
    val personsViewModel = PersonsViewmodel()
    val statusVM = OrderViewmodel()
    val statuses by statusVM.statuses.collectAsState(initial = listOf())
    var person by remember { mutableStateOf<Persons?>(null) }
    LaunchedEffect(key1 = order.personId) {
        personsViewModel.newPersons.collect { persons ->
            person = persons.find { it.id == order.personId }
        }
    }
    var currentState by remember { mutableStateOf(ExpandShapeState.Collapsed) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserHead(
                id = order.name,
                firstName = order.name,
                lastName = order.created_at,
                size = 105.dp,
                textStyle = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    text = order.name,
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Статус: " + statuses.find { it.id == order.status }?.name ,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                val deadline = LocalDateTime.parse(order.deadline)
                val formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())
                val formattedDeadline = formatter.format(deadline.toInstant(TimeZone.of("America/Los_Angeles")).toEpochMilliseconds())
                Text(
                    text = "Срок: 01.12.2023",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        PersonCard(Person = person?: Persons(0, "0", "0", 0), navController = navController )
        Spacer(Modifier.height(16.dp))
        ToggleHeading(Content = { reporting() }, heading = "Отчетность")
    }
}

@Composable
fun reporting() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    {
        //Caurucel placeholder
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
    {
        OutlinedButton(onClick = { /*TODO*/ }) {
            Text("Добавить вещь")
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedButton(onClick = { /*TODO*/ }) {
            Text("Добавить фото")
        }
    }
}