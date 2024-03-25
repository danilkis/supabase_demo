package com.example.supabasedemo.screens.Shelf

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.screens.Shelf.Dialog.AddShelfDialog
import com.example.supabasedemo.viewmodel.Shelf.ShelfViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShelfScreen(navController: NavController, viewModel: ShelfViewmodel = viewModel()) {
    val density = LocalDensity.current
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
        var visibleLoading by remember {
            mutableStateOf(true)
        }
        var visibleCards by remember {
            mutableStateOf(false)
        }
        val shelf = viewModel.shelves.collectAsStateWithLifecycle(initialValue = listOf())
        if (shelf.value.isEmpty()) {
            visibleLoading = true
            visibleCards = false
        } else {
            visibleLoading = false
            visibleCards = true
        }
        AnimatedVisibility(visibleLoading, enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            initialAlpha = 0.2f
        ),
            exit = slideOutVertically(
                animationSpec = tween(
                    durationMillis = 100,
                    easing = EaseInBounce
                )
            )) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(60.dp))
            }
        }
        AnimatedVisibility(visibleCards, enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { 40.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            animationSpec = tween(durationMillis = 400, easing = LinearEasing),
            initialAlpha = 0.2f
        ),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()) {
            ShelfColumn(navController, viewModel, paddingValues)
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