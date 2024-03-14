package com.example.supabasedemo.screens.Things.Dialogs

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
import com.example.supabasedemo.model.Things.Things
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import kotlinx.coroutines.launch

@Composable
fun DeleteThingDialog(
    thing: Things,
    viewModel: ThingsViewmodel = viewModel(),
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
                        coroutineScope.launch { viewModel.deleteThing(thing.id) }
                        openDialog = false
                        onDismiss()
                    }
                ) {
                    Text(text = "Да")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                        onCancel()
                    }
                ) {
                    Text(text = "Нет")
                }
            },
            title = { Text(text = stringResource(R.string.confirmation_question)) },
            text = { Text(text = stringResource(R.string.thing_delete_message, thing.name)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            } // add icon
        )
    }
}