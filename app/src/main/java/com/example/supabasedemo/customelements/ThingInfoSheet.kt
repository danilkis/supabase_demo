package com.example.supabasedemo.customelements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.supabasedemo.model.Orders
import com.example.supabasedemo.model.Things
import com.example.supabasedemo.model.Type
import com.example.supabasedemo.viewmodel.OrderThingsViewmodel
import io.github.jan.supabase.realtime.Column
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingSheet(thing: Things, types: MutableList<Type>, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val painter = rememberImagePainter(data = thing.photoUrl)

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        val orderThingVM = OrderThingsViewmodel()
        orderThingVM.thingId = thing.id
        val orders by orderThingVM.OrderThings.collectAsState(initial = listOf())

        Column(modifier = androidx.compose.ui.Modifier.padding(8.dp)) {
            Image(
                painter = painter,
                contentDescription = "Photo of ${thing.name}",
                contentScale = ContentScale.FillWidth,
                modifier = androidx.compose.ui.Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(16.dp))
            )
            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            Text(text = "Name: ${thing.name}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Store: ${thing.store ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Amount: ${thing.amount}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Type: ${thing.getTypeName(types)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Box ID: ${thing.boxId}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            Text(text = "Используется в заказах", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            LazyColumn(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)
            )
            {
                items(orders)
                {
                    order ->
                    OrderCard(Order = order, navController = rememberNavController())
                }
            }
        }
    }
}