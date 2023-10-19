package com.example.supabasedemo.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.supabasedemo.client
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.viewmodel.PersonsViewmodel
import io.github.jan.supabase.postgrest.postgrest

@Composable
fun PersonScreen(navController: NavController, viewModel: PersonsViewmodel = viewModel())
{
    val persons by viewModel.persons.collectAsState(initial = listOf())
    Column {
        SearchBarCustom()
        Spacer(modifier = Modifier.height(8.dp))
        PersonColumn(persons)
    }
}


@Composable
fun PersonColumn(persons: List<Persons>)
{
    Column()
    {
        for (Pers in persons)
        {
            PersonCard(Pers)
        }
    }
}

@Composable
fun PersonCard(Person: Persons) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp)
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