package com.example.supabasedemo.customelements.Cards

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Shelf.Shelf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShelfCard(shelf: Shelf, navController: NavController, LongClickAction: () -> Unit) {
    //val coroutineScope = rememberCoroutineScope()
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 2.dp)
            .combinedClickable(
                onClick = {
                    navController.navigate("shelf/${shelf.id}")
                    Log.e("NAV", "NAV TO SHELF INFO")
                },
                onLongClick = {
                    LongClickAction()
                }
            )
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
                Row()
                {
                    Text(
                        text = stringResource(R.string.shelf_info_room) + shelf.room,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.shelf_info_floor) + shelf.floor.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}