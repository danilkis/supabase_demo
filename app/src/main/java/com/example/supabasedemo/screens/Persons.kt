package com.example.supabasedemo.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.PersonCard
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.model.Contacts
import com.example.supabasedemo.model.Persons
import com.example.supabasedemo.viewmodel.PersonsViewmodel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun PersonScreen(navController: NavController, viewModel: PersonsViewmodel = viewModel()) {
    val persons by viewModel.newPersons.collectAsState(initial = mutableListOf())
    if (persons.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBarCustom()
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(60.dp))
            }
        }
    } else {
        var openDialog = remember { mutableStateOf(false) }
        Scaffold(modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 70.dp),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        openDialog.value = true
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "add icon")
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                ) {
                    SearchBarCustom()
                    Spacer(modifier = Modifier.height(8.dp))
                    PersonColumn(navController, viewModel)
                }
            }
        )
        if (openDialog.value) {
            AddPersonDialog(openDialog.value, onDismiss = { openDialog.value = false }, viewModel)
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonColumn(navController: NavController, viewModel: PersonsViewmodel = viewModel()) {
    val persons by viewModel.newPersons.collectAsState(initial = mutableListOf())

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(persons) { person ->
            var dismissState = rememberDismissState()
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(persons) {
                dismissState.reset()
            }
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
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
                dismissContent = {
                    PersonCard(person, navController)
                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        DeleteDialog(
                            person = person,
                            viewModel,
                            onDismiss = {
                                coroutineScope.launch {
                                    dismissState.reset()

                                }
                            },
                            onCancel = {
                                coroutineScope.launch { dismissState.reset() }
                            }
                        )
                    }
                }
            )
        }
    }
}


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
                        coroutineScope.launch { viewModel.delete(person.id!!) }
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
                    Text(text = "Отмена")
                }
            },
            title = { Text(text = "Вы уверенны?") },
            text = { Text(text = "Вы собираетесь удалить ${person.Name} ${person.Surname}?") },
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
                        var new_person = Persons(0, name, surname, 0)
                        var new_contact = Contacts(0, phone, telegram, avito)
                        viewModel.deleteComplete.value = true
                        coroutineScope.launch { viewModel.insertContact(new_contact, new_person) }
                        onDismiss()
                    }
                ) {
                    Text(text = "Готово")
                }
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Имя") }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = surname,
                        onValueChange = { surname = it },
                        placeholder = { Text("Фамилия") }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("Номер телефона, формат (+7...)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = telegram,
                        onValueChange = { telegram = it },
                        placeholder = { Text("Ник телеграмм") }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = avito,
                        onValueChange = { avito = it },
                        placeholder = { Text("Ссылка на авито") }
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = "Отмена")
                }
            },
            title = { Text(text = "Добавление нового человека") },
            icon = {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null
                )
            } // add icon
        )
    }
}