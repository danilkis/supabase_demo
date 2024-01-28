package com.example.supabasedemo.customelements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.supabasedemo.R
import com.example.supabasedemo.model.Things
import com.example.supabasedemo.model.Type

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ThingCard(thing: Things, type: MutableList<Type>,onClick: () -> Unit, LongClickAction: () -> Unit ) {
    OutlinedCard(modifier = Modifier.fillMaxWidth().combinedClickable(
            onClick = {
                onClick()
            },
        onLongClick = {
            LongClickAction()
        }
    )) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(modifier = Modifier.padding(8.dp))
            {
                Text(thing.name, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(stringResource(R.string.in_stock_number, thing.amount), style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(thing.getTypeName(type), style = MaterialTheme.typography.bodySmall)
            }
            val photo = thing.photoUrl

            Image(
                painter = rememberAsyncImagePainter(photo),
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .width(120.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}