package com.example.supabasedemo.screens

import android.annotation.SuppressLint
import android.app.Person
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.supabasedemo.client
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Contacts
import com.example.supabasedemo.model.Persons
import io.github.jan.supabase.postgrest.postgrest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchBarCustom() {
    var text by remember { mutableStateOf("") } // Query for SearchBar
    var active by remember { mutableStateOf(false) } // Active state for SearchBar
    val searchHistory = remember { mutableStateListOf("") }

    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        SearchBar(modifier = Modifier.fillMaxWidth(),
            query = text,
            onQueryChange = {
                text = it
            },
            onSearch = {
                searchHistory.add(text)
                active = false
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(text = "Enter your query")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                text = ""
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon"
                    )
                }
            }
        ) {
            searchHistory.forEach {
                if (it.isNotEmpty()) {
                    Row(modifier = Modifier.padding(all = 14.dp)) {
                        Icon(imageVector = Icons.Default.History, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = it)
                    }
                }
            }

            Divider()
            Text(
                modifier = Modifier
                    .padding(all = 14.dp)
                    .fillMaxWidth()
                    .clickable {
                        searchHistory.clear()
                    },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                text = "clear all history"
            )
        }
    }
}
@Composable
fun PersonScreen()
{
    Column {
        SearchBarCustom()
        Spacer(modifier = Modifier.height(16.dp))
        PersonColumn()
    }
}


@Composable
fun PersonColumn()
{
    var Cont = remember { mutableStateListOf<Contacts>()}
    LaunchedEffect(true){
        client.postgrest["Contacts"].select().decodeList<Contacts>().forEach(){
            Cont.add(it)
            Log.e("Supa", it.toString())
        }
    }
    Column()
    {
        for (Pers in Cont)
        {
            PersonCard(Pers)
        }
    }
}

@Composable
fun PersonCard(Person: Contacts) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, top = 1.dp, end = 4.dp, bottom = 1.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserHead(id = "a", firstName = Person.phone, lastName = Person.telegram.toString(), size = 38.dp)
            Spacer(modifier = Modifier.width(2.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            ) {
                Text(
                    text = Person.phone,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = Person.telegram.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}