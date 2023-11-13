package com.example.supabasedemo.customelements

import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.supabasedemo.model.Things

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingCard(thing: Things)
{
    OutlinedCard(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
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
                Text("В наличии: ${thing.amount}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(thing.type.toString(), style = MaterialTheme.typography.bodySmall)
            }
            Image(
                painter = rememberAsyncImagePainter("https://proreiling.ru/wp-content/uploads/5/9/d/59d46a21ac5629beea300f92ad418b98.png"),
                contentDescription = null,
                modifier = Modifier.height(80.dp).width(90.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}