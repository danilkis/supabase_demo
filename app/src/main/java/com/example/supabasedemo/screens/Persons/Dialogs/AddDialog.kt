package com.example.supabasedemo.screens.Persons.Dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supabasedemo.R
import com.example.supabasedemo.model.Persons.Contacts
import com.example.supabasedemo.model.Persons.Persons
import com.example.supabasedemo.viewmodel.Person.PersonsViewmodel
import kotlinx.coroutines.launch

@Composable
fun AddPersonDialog(
    open: Boolean,
    onDismiss: () -> Unit,
    viewModel: PersonsViewmodel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var telegram by remember { mutableStateOf("") }
    var avito by remember { mutableStateOf("") }
    if (open) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val new_person = Persons(0, name, surname, 0)
                        val new_contact = Contacts(0, phone, telegram, avito)
                        viewModel.deleteComplete.value = true
                        coroutineScope.launch { viewModel.insertContact(new_contact, new_person) }
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(R.string.done))
                }
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text(stringResource(R.string.person_name)) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = surname,
                        onValueChange = { surname = it },
                        placeholder = { Text(stringResource(R.string.person_surname)) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text(stringResource(R.string.phone_number_format)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = telegram,
                        onValueChange = { telegram = it },
                        placeholder = { Text(stringResource(R.string.telegram_tag)) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = avito,
                        onValueChange = { avito = it },
                        placeholder = { Text(stringResource(R.string.person_url)) }
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            title = { Text(text = stringResource(R.string.add_person_message)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null
                )
            } // add icon
        )
    }
}