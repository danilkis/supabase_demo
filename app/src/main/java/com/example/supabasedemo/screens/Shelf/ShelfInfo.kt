package com.example.supabasedemo.screens.Shelf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.BoxCard
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Shelf.Shelf
import com.example.supabasedemo.viewmodel.Shelf.ShelfBoxesViewmodel
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel

@Composable
fun ShelfInfoScreen(shelf: Shelf, navController: NavController) { //TODO: Удалить
    val thingVm = ThingsViewmodel()
    val boxes by thingVm.boxes.collectAsState(initial = listOf())
    val shelfBoxesVm = ShelfBoxesViewmodel()
    val shelfBoxes by shelfBoxesVm.shelves_boxes.collectAsState(initial = listOf())
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
                id = shelf.id.toString(),
                firstName = shelf.name,
                lastName = shelf.id.toString(),
                size = 95.dp,
                textStyle = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    text = shelf.name,
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row()
                {
                    Text(
                        text = "Помещение: " + shelf.room,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "Этаж" + shelf.floor.toString(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 6.dp, end = 6.dp)
        )
        {
            val CurrentShelf = shelfBoxes.filter { it.ShelfID == shelf.id }
            items(CurrentShelf.flatMap { shelf -> boxes.filter { shelf.BoxID == it.id } }) {
                BoxCard(it, navController)
            }
        }
        Row(horizontalArrangement = Arrangement.Center)
        {
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text("Добавить вещь")
            }
        }
    }
}
