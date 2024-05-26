package com.example.supabasedemo.screens.Things.Dialogs

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supabasedemo.R
import com.example.supabasedemo.model.Things.Box
import com.example.supabasedemo.model.Things.Things
import com.example.supabasedemo.model.Things.Type
import com.example.supabasedemo.supabase.BucketWorker
import com.example.supabasedemo.supabase.supaHelper
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddThingDialog(
    open: Boolean,
    onDismiss: () -> Unit,
    viewModel: ThingsViewmodel = viewModel()
) { //TODO: Навигация на добавление товара
    val coroutineScope = rememberCoroutineScope()
    val contentResolver = LocalContext.current.contentResolver
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var storeUrl by remember { mutableStateOf("") }
    var filePath by remember { mutableStateOf("") }
    val chosenType by remember { mutableStateOf(mutableStateOf(Type(0, ""))) }
    val chosenBox by remember { mutableStateOf(mutableStateOf(Box("", "", ""))) }

    var nameError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var typeError by remember { mutableStateOf(false) }
    var boxError by remember { mutableStateOf(false) }

    val ctx = LocalContext.current
    if (open) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        var boxuuid = chosenBox.value.id
                        Log.e("BoxVal", chosenBox.value.toString())
                        if (chosenBox.value.id.isNullOrBlank()) {
                            boxuuid = "00000000-0000-0000-0000-000000000000"
                        }
                        coroutineScope.launch {
                            val photo = BucketWorker().UploadFile(filePath, contentResolver)
                            if (!photo.isNullOrBlank()) {
                                viewModel.insertThing(
                                    Things(
                                        "",
                                        name,
                                        storeUrl,
                                        amount.toInt(),
                                        chosenType.value.id,
                                        photo,
                                        boxuuid,
                                        supaHelper.userUUID
                                    )
                                )
                            }
                        }
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(R.string.done))
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = it.isNullOrBlank()
                        },
                        isError = nameError,
                        label = { Text(stringResource(R.string.name)) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = amount,
                        onValueChange = {
                            amount = it
                            amountError = it.isNullOrBlank().and(!it.isDigitsOnly())
                        },
                        isError = amountError,
                        label = { Text(stringResource(R.string.in_stock)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = storeUrl,
                        onValueChange = { storeUrl = it },
                        label = { Text(stringResource(R.string.store_url)) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    val boxesList = viewModel.boxes.collectAsState(initial = listOf<Box>()).value
                    var boxFieldValue by remember { mutableStateOf("") }
                    var expanded1 by remember { mutableStateOf(false) }
                    // container for textfield and menu
                    ExposedDropdownMenuBox( // Коробка
                        expanded = expanded1,
                        onExpandedChange = {
                            expanded1 = !expanded1
                        }
                    ) {
                        // textfield
                        OutlinedTextField(
                            modifier = Modifier
                                .menuAnchor(), // menuAnchor modifier must be passed to the text field for correctness
                            value = boxFieldValue,
                            onValueChange = { newValue ->
                                boxFieldValue = newValue
                            },
                            label = { Text(stringResource(R.string.box)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded1) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        )

                        // filter options based on text field value
                        val filteringOptions =
                            boxesList.filter { it.name.contains(boxFieldValue, ignoreCase = true) }
                        if (filteringOptions.isNotEmpty()) {
                            ExposedDropdownMenu(
                                expanded = expanded1,
                                onDismissRequest = { expanded1 = false },
                            ) {
                                filteringOptions.forEach { selectedBox ->
                                    DropdownMenuItem(
                                        text = { Text(selectedBox.name) },
                                        onClick = {
                                            boxFieldValue = selectedBox.name
                                            chosenBox.value = selectedBox
                                            expanded1 = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }

                    val typesList = viewModel.types.collectAsState(initial = listOf<Type>()).value
                    var textFieldValue by remember { mutableStateOf("") }
                    var expanded by remember { mutableStateOf(false) }
                    // container for textfield and menu
                    ExposedDropdownMenuBox( //Тип
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        // textfield
                        OutlinedTextField(
                            modifier = Modifier
                                .menuAnchor(), // menuAnchor modifier must be passed to the text field for correctness
                            value = textFieldValue,
                            onValueChange = { newValue ->
                                textFieldValue = newValue
                            },
                            label = { Text(stringResource(R.string.type)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        )

                        // filter options based on text field value
                        val filteringOptions =
                            typesList.filter { it.Name.contains(textFieldValue, ignoreCase = true) }
                        if (filteringOptions.isNotEmpty()) {
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                filteringOptions.forEach { selectedMovie_ ->
                                    DropdownMenuItem(
                                        text = { Text(selectedMovie_.Name) },
                                        onClick = {
                                            textFieldValue = selectedMovie_.Name
                                            chosenType.value = selectedMovie_
                                            expanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }

                    val filePickerLauncher =
                        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                            // Handle the result from the file picker dialog
                            filePath = uri.toString()
                        }

                    OutlinedButton(onClick = {
                        // Launch the file picker dialog when the button is pressed
                        filePickerLauncher.launch("image/*")
                    }) {
                        Text(stringResource(R.string.choose_photo))
                    }
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
            title = { Text(text = stringResource(R.string.add_thing_message)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            } // add icon
        )
    }
}
