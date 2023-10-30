package com.example.supabasedemo.screens

import android.app.Person
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Contacts
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.viewmodel.PersonInfoViewmodel
import com.example.supabasedemo.viewmodel.PersonInfoViewmodelFactory

@Composable
fun PersonInfoScreen(person: Persons, navController: NavController, viewModel: PersonInfoViewmodel = androidx.lifecycle.viewmodel.compose.viewModel {
    PersonInfoViewmodelFactory(person).create(PersonInfoViewmodel::class.java)
})
{
    val contact = PersonInfoViewmodel(person).contacts.collectAsState(initial = Contacts(0, "0","0", "0"))

    if (contact.value == Contacts(0, "0","0", "0")) {
        CircularProgressIndicator(modifier = Modifier.size(60.dp))
    }
    else
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                UserHead(id = person.id.toString(), firstName = person.Name, lastName = person.Surname.toString(), size = 80.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column (modifier = Modifier.padding(5.dp), verticalArrangement = Arrangement.Center) {
                    Text(text = person.Name,
                        style = MaterialTheme.typography.headlineLarge)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = person.Surname.toString(),
                        style = MaterialTheme.typography.headlineSmall)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Контакты",
                style = MaterialTheme.typography.displaySmall)
            Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {
                ContactCard(contactName = "Phone", value = contact.value.phone )
                Spacer(modifier = Modifier.height(10.dp))
                if (contact.value.url.isNullOrBlank())
                {
                    ContactCard(contactName = "Telegram", value = contact.value.telegram.toString())
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (contact.value.url.isNullOrBlank())
                {
                    ContactCard(contactName = "Url", value = contact.value.url.toString() )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun ContactCard(contactName: String, value: String)
{
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 4.dp, end = 10.dp, bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserHead(id = "a", firstName = contactName, lastName = "", size = 40.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}