package com.example.supabasedemo.customelements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.BoxCard
import com.example.supabasedemo.model.Things.Box
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxSheet(
    onDismiss: () -> Unit,
    onChosen: (Box) -> Unit,
    navController: NavController,
    viewModel: ThingsViewmodel = viewModel()
) {
    val ctx = LocalContext.current
    val boxes by viewModel.boxes.collectAsState(initial = mutableListOf())
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.Center) {
            Text(
                text = "Выберите коробку",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
            )
            {
                items(boxes) { it ->
                    BoxCard(box = it, navController = navController, onClick = { onChosen(it) })
                }
            }
        }
    }
}