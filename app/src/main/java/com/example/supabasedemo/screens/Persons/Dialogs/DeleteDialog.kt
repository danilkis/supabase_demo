package com.example.supabasedemo.screens.Persons.Dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supabasedemo.R
import com.example.supabasedemo.model.Persons.Persons
import com.example.supabasedemo.viewmodel.Person.PersonsViewmodel
import kotlinx.coroutines.launch

@Composable
fun DeleteDialog(
    person: Persons,
    viewModel: PersonsViewmodel = viewModel(),
    onDismiss: () -> Unit,
    onCancel: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var openDialog by remember { mutableStateOf(true) }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
                onCancel()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch { viewModel.deletePerson(person.id) }
                        openDialog = false
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                        onCancel()
                    }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            title = { Text(text = stringResource(R.string.confirmation_question)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.person_delete_message,
                        person.Name,
                        person.Surname.toString()
                    )
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            } // add icon
        )
    }
}