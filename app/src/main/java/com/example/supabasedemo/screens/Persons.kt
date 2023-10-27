package com.example.supabasedemo.screens

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Contacts
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.viewmodel.PersonsViewmodel
import io.github.jan.supabase.postgrest.postgrest

@Composable
fun PersonScreen(navController: NavController, viewModel: PersonsViewmodel = viewModel())
{
    val persons by viewModel.persons.collectAsState(initial = listOf())
    if (persons.isEmpty()) {
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
            PersonColumn(persons, navController)
        }
    }
}



@Composable
fun PersonColumn(persons: List<Persons>, navController: NavController)
{
    Column()
    {
        for (Pers in persons)
        {
            PersonCard(Pers, navController)
        }
    }
}

@Composable
fun PersonCard(Person: Persons, navController: NavController) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp)
            .clickable {
                navController.navigate("person/${Person.id}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserHead(id = "a", firstName = Person.Name, lastName = Person.Surname.toString(), size = 40.dp)
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
            ) {
                Text(
                    text = Person.Name + " " + Person.Surname.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}