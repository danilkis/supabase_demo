package com.example.supabasedemo.screens.Shelf

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.ShelfCard
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.screens.Shelf.Dialog.AddShelfDialog
import com.example.supabasedemo.screens.Shelf.Dialog.DeleteShelfDialog
import com.example.supabasedemo.screens.Shelf.Dialog.UpdateShelfDialog
import com.example.supabasedemo.viewmodel.Shelf.ShelfViewmodel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrdersMainScreen(navController: NavController, viewModel: ShelfViewmodel = viewModel()) {
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
        val shelf = viewModel.shelves.collectAsStateWithLifecycle(initialValue = listOf())
        if (shelf.value.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(60.dp))
                }
            }
        } else {
            var columnAppeared by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                columnAppeared = true
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues), verticalArrangement = Arrangement.spacedBy(5.dp)
            )
            {
                items(shelf.value) { shelf ->
                    val EditDialogState = remember { mutableStateOf(false) }
                    val dismissState = rememberDismissState()
                    val coroutineScope = rememberCoroutineScope()
                    val dismissDirection = dismissState.dismissDirection
                    var isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
                    // check if the user swiped
                    if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                        DeleteShelfDialog(
                            shelf,
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
                                ShelfCard(
                                    shelf,
                                    navController,
                                    { EditDialogState.value = true })

                                if (EditDialogState.value) {
                                    UpdateShelfDialog(
                                        open = EditDialogState.value,
                                        onDismiss = { EditDialogState.value = false },
                                        shelf = shelf
                                    )
                                }
                            })
                    }
                }
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
}