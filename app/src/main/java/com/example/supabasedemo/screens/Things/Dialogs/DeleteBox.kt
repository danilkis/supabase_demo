package com.example.supabasedemo.screens.Things.Dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supabasedemo.R
import com.example.supabasedemo.model.Box
import com.example.supabasedemo.viewmodel.ThingsViewmodel
import kotlinx.coroutines.launch

@Composable
fun DeleteBoxDialog(
    box: Box,
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
                        coroutineScope.launch { viewModel.deleteBox(box.id) }
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
            text = { Text(text = stringResource(R.string.box_delete_message, box.name)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            } // add icon
        )
    }
}