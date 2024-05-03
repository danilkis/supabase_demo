package com.example.supabasedemo.screens.Shelf.Dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supabasedemo.R
import com.example.supabasedemo.model.Shelf.Shelf
import com.example.supabasedemo.viewmodel.Shelf.ShelfViewmodel
import kotlinx.coroutines.launch

@Composable
fun AddShelfDialog(
    open: Boolean,
    onDismiss: () -> Unit,
    viewModel: ShelfViewmodel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var availablelevels by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    if (open) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val new_shelf =
                            Shelf("", name, availablelevels.toInt(), room, floor.toInt(), "")
                        coroutineScope.launch { viewModel.insertShelf(new_shelf) }
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
                        placeholder = { Text(stringResource(R.string.shelf_name)) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = availablelevels,
                        onValueChange = { availablelevels = it },
                        placeholder = { Text(stringResource(R.string.shelf_levels)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = room,
                        onValueChange = { room = it },
                        placeholder = { Text(stringResource(R.string.shelf_room)) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = floor,
                        onValueChange = { floor = it },
                        placeholder = { Text(stringResource(R.string.shelf_floor)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
            title = { Text(text = stringResource(R.string.shelf_add_new)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.AllInbox,
                    contentDescription = null
                )
            } // add icon
        )
    }
}