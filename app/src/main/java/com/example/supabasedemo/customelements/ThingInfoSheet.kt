package com.example.supabasedemo.customelements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.Cards.OrderCard
import com.example.supabasedemo.model.Things.Things
import com.example.supabasedemo.model.Things.Type
import com.example.supabasedemo.viewmodel.Order.OrderThingsViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingSheet(
    thing: Things,
    types: MutableList<Type>,
    onDismiss: () -> Unit,
    navController: NavController
) {
    val painter = rememberAsyncImagePainter(model = thing.photoUrl)
    val ctx = LocalContext.current
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
            Text(
                text = stringResource(R.string.name_thingInfo, thing.name),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.store_thinginfo, thing.store ?: "N/A"),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.amount_thinginfo, thing.amount),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(
                    R.string.type_thinginfo,
                    stringResource(thing.getTypeName(types, ctx))
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.boxID_thinginfo, thing.boxId),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.usedInOrders),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            LazyColumn(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)
            )
            {
                items(orders)
                { order ->
                    OrderCard(Order = order, navController = navController)
                }
            }
        }
    }
}