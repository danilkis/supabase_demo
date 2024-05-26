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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.supabasedemo.R
import com.example.supabasedemo.model.Things.Things
import com.example.supabasedemo.model.Things.Type
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingSheet(
    thing: Things,
    types: List<Type>,
    onDismiss: () -> Unit,
    navController: NavController,
    viewModel: ThingsViewmodel = viewModel()
) {
    val painter = rememberAsyncImagePainter(model = thing.photoUrl)
    val ctx = LocalContext.current
    val boxes by viewModel.boxes.collectAsState(initial = mutableListOf())
    val current_box = boxes.findLast { it.id == thing.boxId }
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Column(modifier = androidx.compose.ui.Modifier.padding(8.dp)) {
            if (!thing.photoUrl.isNullOrBlank()) {
                if (isValidUrl(thing.photoUrl)) {
                    Image(
                        painter = painter,
                        contentDescription = "Photo of ${thing.name}",
                        contentScale = ContentScale.FillWidth,
                        modifier = androidx.compose.ui.Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(16.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
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
                text = stringResource(R.string.boxID_thinginfo, current_box?.name ?: " "),
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

fun isValidUrl(url: String): Boolean {
    // Регулярное выражение для проверки URL
    val regex =
        """https://supabase.pavlovskhome.ru/storage/v1/object/public/photos/[A-Za-z0-9._-]+\.jpg"""
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(url)
    return matcher.matches()
}