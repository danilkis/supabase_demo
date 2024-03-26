package com.example.supabasedemo.screens.Persons

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.screens.Persons.Dialogs.AddPersonDialog
import com.example.supabasedemo.viewmodel.Person.PersonsViewmodel

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun PersonScreen(navController: NavController, viewModel: PersonsViewmodel = viewModel()) {

    val persons by viewModel.newPersons.collectAsState(initial = mutableListOf())
    val openDialog = remember { mutableStateOf(false) }
    Scaffold(topBar = { SearchBarCustom(navController) }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                openDialog.value = true
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add icon")
        }
    })
    { paddingValues ->
        var visibleLoading by remember {
            mutableStateOf(true)
        }
        var visibleCards by remember {
            mutableStateOf(false)
        }
        if (persons.isEmpty()) {
            visibleLoading = true
            visibleCards = false
        } else {
            visibleLoading = false
            visibleCards = true
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PersonPanes(navController = navController, viewModel)
        }
        if (openDialog.value) {
            AddPersonDialog(
                openDialog.value,
                onDismiss = { openDialog.value = false },
                viewModel
            )
        }
    }
}
