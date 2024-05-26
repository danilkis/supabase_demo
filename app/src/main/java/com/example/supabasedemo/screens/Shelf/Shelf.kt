package com.example.supabasedemo.screens.Shelf

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.screens.Shelf.Dialog.AddShelfDialog
import com.example.supabasedemo.viewmodel.Shelf.ShelfViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShelfScreen(navController: NavController, viewModel: ShelfViewmodel = viewModel()) {
    val density = LocalDensity.current
    val shelfes by viewModel.shelves.collectAsState(initial = mutableListOf())
    val openDialog = remember { mutableStateOf(false) }
    //val ctx = LocalContext.current
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
        if (shelfes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.Empty_add_something))
                }
            }
        } else {
            ShelfPane(navController, viewModel, paddingValues)
        }
    }
    if (openDialog.value) {
        AddShelfDialog(
            openDialog.value,
            onDismiss = { openDialog.value = false },
            viewModel
        )
    }
}