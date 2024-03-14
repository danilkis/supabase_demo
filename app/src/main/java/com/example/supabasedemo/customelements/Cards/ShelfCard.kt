package com.example.supabasedemo.customelements.Cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Shelf.Shelf

@Composable
fun ShelfCard(shelf: Shelf, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 2.dp)
            .clickable {
                navController.navigate("shelf/${shelf.id}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserHead(
                id = "a",
                firstName = shelf.name,
                lastName = shelf.id.toString(),
                size = 45.dp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
            ) {
                Text(
                    text = shelf.name,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Помещение: " + shelf.room,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}