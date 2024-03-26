package com.example.supabasedemo.customelements.Cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Persons.Persons

@Composable
fun ContactInfoHeader(person: Persons) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserHead(
            id = person.id.toString(),
            firstName = person.Name,
            lastName = person.Surname.toString(),
            size = 80.dp,
            textStyle = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.padding(5.dp), verticalArrangement = Arrangement.Center) {
            Text(
                text = person.Name,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = person.Surname.toString(),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}