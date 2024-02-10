package com.example.supabasedemo.screens.Persons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.Cards.ContactCard
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Contacts
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.viewmodel.PersonInfoViewmodel
import com.example.supabasedemo.viewmodel.PersonInfoViewmodelFactory


@Composable
fun PersonInfoScreen(
    person: Persons,
    navController: NavController,
    viewModel: PersonInfoViewmodel = androidx.lifecycle.viewmodel.compose.viewModel {
        PersonInfoViewmodelFactory(person).create(PersonInfoViewmodel::class.java)
    }
) {
    val contact = viewModel.contacts.collectAsState(Contacts(0, "0", "0", "0")).value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
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