package com.example.supabasedemo.customelements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.supabasedemo.R
import com.example.supabasedemo.model.Things.Things
import com.example.supabasedemo.model.Things.Type
import com.example.supabasedemo.viewmodel.Order.OrderThingsViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingSheet(
    thing: Things,
    types: List<Type>,
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
                text = stringResource(R.string.boxID_thinginfo, thing.boxId ?: " "),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )
            {
                GenerateQRButton(path = "T" + thing.id)
            }
        }
    }
}