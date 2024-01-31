package com.example.supabasedemo.screens.Things.Dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supabasedemo.R
import com.example.supabasedemo.model.Box
import com.example.supabasedemo.viewmodel.ThingsViewmodel
import kotlinx.coroutines.launch

@Composable
fun AddBoxDialog(
    open: Boolean,
    onDismiss: () -> Unit,
    viewModel: ThingsViewmodel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    if (open) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val box = Box(0, name, barcode)
                        viewModel.deleteComplete.value = true
                        coroutineScope.launch { viewModel.insertBoxes(box) }
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
                        placeholder = { Text(stringResource(R.string.name)) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = barcode,
                        onValueChange = { barcode = it },
                        placeholder = { Text(stringResource(R.string.barcode)) }
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
            title = { Text(text = stringResource(R.string.add_box_message)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.AllInbox,
                    contentDescription = null
                )
            } // add icon
        )
    }
}
