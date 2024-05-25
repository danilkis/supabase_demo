package com.example.supabasedemo.screens.Persons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.Cards.ContactCard
import com.example.supabasedemo.customelements.Cards.ContactInfoHeader
import com.example.supabasedemo.customelements.Cards.LoadingCard
import com.example.supabasedemo.model.Persons.Contacts
import com.example.supabasedemo.model.Persons.Persons

@Composable
fun Info() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.ClickOnCard))
    }
}

@Composable
fun PersonLoading(person: Persons) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        ContactInfoHeader(person)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.contacts),
            style = MaterialTheme.typography.displaySmall
        )
        LoadingCard()
        Spacer(modifier = Modifier.height(10.dp))
        LoadingCard()
        Spacer(modifier = Modifier.height(10.dp))
        LoadingCard()
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun PersonLoaded(person: Persons, contact: Contacts) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {
        ContactInfoHeader(person = person)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.contacts),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ContactCard(contactName = "Phone", value = contact.phone)
            Spacer(modifier = Modifier.height(10.dp))
            if (!contact.telegram.isNullOrBlank()) {
                ContactCard(contactName = "Telegram", value = contact.telegram.toString())
                Spacer(modifier = Modifier.height(10.dp))
            }
            if (!contact.url.isNullOrBlank()) {
                ContactCard(contactName = "Url", value = contact.url.toString())
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}