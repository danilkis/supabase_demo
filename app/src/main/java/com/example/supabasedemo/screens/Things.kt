package com.example.supabasedemo.screens

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.BoxCard
import com.example.supabasedemo.customelements.OptionsFAB
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.customelements.ThingCard
import com.example.supabasedemo.customelements.ThingSheet
import com.example.supabasedemo.customelements.ToggleHeading
import com.example.supabasedemo.model.Box
import com.example.supabasedemo.model.Things
import com.example.supabasedemo.model.Type
import com.example.supabasedemo.supa.BucketWorker
import com.example.supabasedemo.viewmodel.ThingsViewmodel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ThingsMainScreen(navController: NavController, viewModel: ThingsViewmodel = viewModel()) {
    val things by viewModel.things.collectAsState(initial = mutableListOf())
    val openDialogThing = remember { mutableStateOf(false) }
    val openDialogBox = remember { mutableStateOf(false) }
    Scaffold(topBar = { SearchBarCustom(navController) }, floatingActionButton = {
        OptionsFAB({openDialogBox.value = true}, {openDialogThing.value = true})
    })
    { paddingValues ->
    if (things.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding())) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(60.dp))
            }
        }
    } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding(), start = 6.dp, end = 6.dp)
                        .fillMaxWidth()
                ) {
                    ToggleHeading({ BoxColumn(navController = navController, viewModel) },
                        stringResource(
                            R.string.boxes
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ToggleHeading({ ThingColumn(navController = navController, viewModel) },
                        stringResource(
                            R.string.unsorted
                        )
                    )
                }
            }
        if (openDialogThing.value) {
            AddThingDialog(openDialogThing.value, onDismiss = { openDialogThing.value = false }, viewModel)
        }
        if (openDialogBox.value) {
            AddBoxDialog(openDialogBox.value, onDismiss = { openDialogBox.value = false }, viewModel)
        }
    }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxColumn(navController: NavController, viewModel: ThingsViewmodel = viewModel()) {
    val boxes by viewModel.boxes.collectAsState(initial = mutableListOf())
    var columnAppeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        columnAppeared = true
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)
    )
    {
        items(boxes.filter { it.id != 0 }) { box ->
            val dismissState = rememberDismissState()
            val coroutineScope = rememberCoroutineScope()
            val dismissDirection = dismissState.dismissDirection
            var isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            // check if the user swiped
            if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                DeleteBoxDialog(
                    box,
                    viewModel,
                    onDismiss = {
                        isDismissed = true; coroutineScope.launch { dismissState.reset() }
                    },
                    onCancel = {
                        isDismissed = false; coroutineScope.launch { dismissState.reset() }
                    })
            }

            var itemAppeared by remember { mutableStateOf(!columnAppeared) }
            LaunchedEffect(Unit) {
                itemAppeared = true
            }
            if (viewModel.deleteComplete.value && itemAppeared && !isDismissed) {
                viewModel.deleteComplete.value = false
            }
            AnimatedVisibility(
                visible = itemAppeared && !viewModel.deleteComplete.value,
                exit = fadeOut(
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMedium)
                ) + scaleOut(
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
                ),
                enter = fadeIn(
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMedium)
                ) + scaleIn(
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
                )
            ) {
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(
                        DismissDirection.EndToStart
                    ),
                    background = {
                        val backgroundColor by animateColorAsState(
                            when (dismissState.targetValue) {
                                DismissValue.DismissedToStart -> MaterialTheme.colorScheme.error.copy(
                                    alpha = 0.8f
                                )

                                else -> MaterialTheme.colorScheme.background
                            }
                        )

                        // icon size
                        val iconScale by animateFloatAsState(
                            targetValue = if (dismissState.targetValue == DismissValue.DismissedToStart) 1.3f else 0.5f
                        )

                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color = backgroundColor)
                                .padding(end = 16.dp), // inner padding
                            contentAlignment = Alignment.CenterEnd // place the icon at the end (left)
                        ) {
                            Icon(
                                modifier = Modifier.scale(iconScale),
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    },
                    dismissContent =
                    {
                        BoxCard(box = box, navController = navController)
                    })
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingColumn(navController: NavController, viewModel: ThingsViewmodel = viewModel()) {
    val things by viewModel.things.collectAsState(initial = mutableListOf())
    val types by viewModel.types.collectAsState(initial = mutableListOf())
    var columnAppeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        columnAppeared = true
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)
    )
    {
        items(things.filter { it.boxId == 1 }) { thing ->
            val ModalSheetState = remember { mutableStateOf(false) }
            val EditDialogState = remember { mutableStateOf(false) }
            val dismissState = rememberDismissState()
            val coroutineScope = rememberCoroutineScope()
            val dismissDirection = dismissState.dismissDirection
            var isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            // check if the user swiped
            if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                DeleteThingDialog(
                    thing,
                    viewModel,
                    onDismiss = {
                        isDismissed = true; coroutineScope.launch { dismissState.reset() }
                    },
                    onCancel = {
                        isDismissed = false; coroutineScope.launch { dismissState.reset() }
                    })
            }

            var itemAppeared by remember { mutableStateOf(!columnAppeared) }
            LaunchedEffect(Unit) {
                itemAppeared = true
            }
            if (viewModel.deleteComplete.value && itemAppeared && !isDismissed) {
                viewModel.deleteComplete.value = false
            }
            AnimatedVisibility(
                visible = itemAppeared && !viewModel.deleteComplete.value,
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 300,
                    )
                ) + scaleOut(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                ),
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                ) + scaleIn(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )
            ) {
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(
                        DismissDirection.EndToStart
                    ),
                    background = {
                        val backgroundColor by animateColorAsState(
                            when (dismissState.targetValue) {
                                DismissValue.DismissedToStart -> MaterialTheme.colorScheme.error.copy(
                                    alpha = 0.8f
                                )

                                else -> MaterialTheme.colorScheme.background
                            }
                        )

                        // icon size
                        val iconScale by animateFloatAsState(
                            targetValue = if (dismissState.targetValue == DismissValue.DismissedToStart) 1.3f else 0.5f
                        )

                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color = backgroundColor)
                                .padding(end = 16.dp), // inner padding
                            contentAlignment = Alignment.CenterEnd // place the icon at the end (left)
                        ) {
                            Icon(
                                modifier = Modifier.scale(iconScale),
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    },
                    dismissContent =
                    {
                            ThingCard(thing, types, {ModalSheetState.value = true}, {EditDialogState.value = true})
                            if (EditDialogState.value)
                            {

                            }
                            /*
                            AnimatedVisibility(visible = ModalSheetState.value)
                            {
                                ThingSheet(thing = thing, types, {ModalSheetState.value = false}) TODO: ПОЧИНИТЬ
                            }
                             */
                    })
            }
        }
    }
}

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
    val chosenBox by remember { mutableStateOf(mutableStateOf(Box(0, "", null))) }
    val ctx = LocalContext.current
    if (open) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            val photo = BucketWorker().UploadFile(filePath, contentResolver, ctx)
                            if (!photo.isNullOrBlank()) {
                                viewModel.insertThing(
                                    Things(
                                        0,
                                        name,
                                        storeUrl,
                                        amount.toInt(),
                                        chosenType.value.id,
                                        photo,
                                        chosenBox.value.id
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
                    val boxesList = viewModel.boxes.collectAsState(initial = listOf<Box>()).value
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

                    val typesList = viewModel.types.collectAsState(initial = listOf<Type>()).value
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateThingDialog(
    open: Boolean,
    onDismiss: () -> Unit,
    viewModel: ThingsViewmodel = viewModel(),
    thing: Things //TODO: Воткнуть
) {
    val coroutineScope = rememberCoroutineScope()
    val contentResolver = LocalContext.current.contentResolver
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var storeUrl by remember { mutableStateOf("") }
    var filePath by remember { mutableStateOf("") }
    val chosenType by remember { mutableStateOf(mutableStateOf(Type(0, ""))) }
    val chosenBox by remember { mutableStateOf(mutableStateOf(Box(0, "", null))) }
    val ctx = LocalContext.current
    if (open) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            val photo = BucketWorker().UploadFile(filePath, contentResolver, ctx)
                            if (!photo.isNullOrBlank()) {
                                viewModel.insertThing(
                                    Things(
                                        0,
                                        name,
                                        storeUrl,
                                        amount.toInt(),
                                        chosenType.value.id,
                                        photo,
                                        chosenBox.value.id
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
                    val boxesList = viewModel.boxes.collectAsState(initial = listOf<Box>()).value
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

                    val typesList = viewModel.types.collectAsState(initial = listOf<Type>()).value
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