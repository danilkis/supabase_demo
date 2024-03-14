package com.example.supabasedemo.screens.Things.Dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supabasedemo.R
import com.example.supabasedemo.model.Things.Box
import com.example.supabasedemo.model.Things.Things
import com.example.supabasedemo.model.Things.Type
import com.example.supabasedemo.supabase.BucketWorker
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState", "DiscouragedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateThingDialog(
    open: Boolean,
    onDismiss: () -> Unit,
    viewModel: ThingsViewmodel = viewModel(),
    thing: Things //TODO: Воткнуть
) {
    val boxesList = viewModel.boxes.collectAsState(initial = listOf<Box>()).value
    val typesList = viewModel.types.collectAsState(initial = listOf<Type>()).value

    val coroutineScope = rememberCoroutineScope()
    val contentResolver = LocalContext.current.contentResolver
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var storeUrl by remember { mutableStateOf("") }
    val filePath by remember { mutableStateOf("") }
    val chosenType by remember { mutableStateOf(mutableStateOf(Type(0, ""))) }
    val chosenBox by remember { mutableStateOf(mutableStateOf(Box(0, "", null))) }
    val ctx = LocalContext.current
    name = thing.name
    amount = thing.amount.toString()
    storeUrl = thing.store ?: ""

    if (open) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            var photo = ""
                            photo = if (filePath.isNullOrBlank()) {
                                BucketWorker().UploadFile(filePath, contentResolver)
                            } else {
                                thing.photoUrl ?: ""
                            }
                            viewModel.updateThing(
                                Things(
                                    thing.id,
                                    name,
                                    storeUrl,
                                    amount.toInt(),
                                    chosenType.value.id,
                                    photo,
                                    chosenBox.value.id
                                )
                            )
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
                        onValueChange = { name = it },
                        placeholder = { Text(stringResource(R.string.name)) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        placeholder = { Text(stringResource(R.string.in_stock)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = storeUrl,
                        onValueChange = { storeUrl = it },
                        placeholder = { Text(stringResource(R.string.store_url)) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    var boxFieldValue by remember { mutableStateOf("") }
                    var expanded1 by remember { mutableStateOf(false) }
                    // container for textfield and menu
                    ExposedDropdownMenuBox(
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
                    var textFieldValue by remember { mutableStateOf("") }
                    var expanded by remember { mutableStateOf(false) }
                    // container for textfield and menu
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        // textfield
                        OutlinedTextField(
                            modifier = Modifier
                                .menuAnchor(),
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

                                    val stringKey =
                                        typesList.find { it.id == selectedMovie_.id }?.Name
                                            ?: "nothing" //TODO: Переработать

                                    var res = ctx.resources
                                        .getIdentifier(stringKey, "string", ctx.packageName)
                                    if (res == 0) {
                                        res = R.string.yes
                                    }

                                    DropdownMenuItem(
                                        text = { Text(selectedMovie_.Name) }, //
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
            title = { Text(text = stringResource(R.string.modify_thing)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            } // add icon
        )
    }
}