package com.example.supabasedemo.screens.Shelf

import android.annotation.SuppressLint
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.ShelfCard
import com.example.supabasedemo.model.Shelf.HolderSaverShelf
import com.example.supabasedemo.model.Shelf.Shelf
import com.example.supabasedemo.model.States
import com.example.supabasedemo.screens.Persons.Info
import com.example.supabasedemo.screens.Shelf.Dialog.DeleteShelfDialog
import com.example.supabasedemo.viewmodel.Shelf.ShelfBoxesViewmodel
import com.example.supabasedemo.viewmodel.Shelf.ShelfViewmodel
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ShelfPane(
    navController: NavController, shelfViewmodel: ShelfViewmodel,
    paddingValues: PaddingValues
) {
    // Currently selected item
    //var selectedItem: Persons? by rememberSaveable(stateSaver = Persons) { mutableStateOf(null) }
    var selectedItem: Shelf by rememberSaveable(stateSaver = HolderSaverShelf) {
        mutableStateOf(Shelf("", "", 0, "0", 0, ""))
    }

// Create the ListDetailPaneScaffoldState
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    ShelfList(
                        onItemClick = { id ->
                            selectedItem = id
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        },
                        navController = navController,
                        viewModel = shelfViewmodel
                    )
                }

            }
        },
        detailPane = {
            // Show the detail pane content if selected item is available
            selectedItem.let { item ->
                AnimatedPane(
                    modifier = Modifier
                        .preferredWidth(370.dp)
                        .padding(top = 60.dp)
                ) {
                    ShelfInfo(item, shelfViewmodel, navController)
                }
            }
        },
    )
}

@Composable
fun ShelfInfo(
    shelf: Shelf,
    viewModel: ShelfViewmodel = viewModel(),
    navController: NavController
) {
    var currentState by remember { mutableStateOf(States.Empty) }
    val thingVm = ThingsViewmodel()
    val boxes by thingVm.boxes.collectAsStateWithLifecycle(initialValue = listOf())
    val shelfBoxesVm = ShelfBoxesViewmodel()
    val shelfBoxes by shelfBoxesVm.shelves_boxes.collectAsStateWithLifecycle(initialValue = listOf())
    val CurrentShelf = shelfBoxes.filter { it.ShelfID == shelf.id }

    Crossfade(targetState = currentState, animationSpec = tween(300, 100)) { state ->
        when (state) {
            States.Info -> Info()
            States.Loading -> ShelfLoading(shelf, navController)
            States.Loaded -> ShelfLoaded(shelf = shelf, navController = navController)
            States.Empty -> Box {}
        }
    }
    currentState = if (shelf.id.isNullOrBlank()) {
        States.Info
    } else {
        States.Loaded
    }
}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfList(
    navController: NavController,
    viewModel: ShelfViewmodel = viewModel(),
    onItemClick: (Shelf) -> Unit
) {
    val EditDialogState = remember { mutableStateOf(false) }
    val shelves by viewModel.shelves.collectAsState(initial = mutableListOf()) //TODO: Добавить skeleton loader
    var columnAppeared by remember { mutableStateOf(false) }
    var unread by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        columnAppeared = true
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)
    )
    {
        items(shelves) { shelf ->
            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { distance -> distance * .25f }
            )
            val vibrator = LocalContext.current.getSystemService(Vibrator::class.java)
            val coroutineScope = rememberCoroutineScope()
            // check if the user swiped
            if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
// Create an array of timings in milliseconds
                DeleteShelfDialog(
                    shelf,
                    viewModel,
                    onDismiss = {
                        unread = false; coroutineScope.launch { dismissState.reset() }
                    },
                    onCancel = {
                        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
                        unread = true; coroutineScope.launch { dismissState.reset() }
                    })
            }

            var itemAppeared by remember { mutableStateOf(!columnAppeared) }
            LaunchedEffect(Unit) {
                itemAppeared = true
            }
            if (viewModel.deleteComplete.value && itemAppeared && unread) {
                viewModel.deleteComplete.value = false
            }
            AnimatedVisibility(
                visible = itemAppeared && !viewModel.deleteComplete.value,
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = EaseOut
                    )
                ),
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = EaseIn
                    )
                )
            ) {
                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        val direction = dismissState.dismissDirection
                        val color by animateColorAsState(
                            when (dismissState.targetValue) {
                                SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.background
                                SwipeToDismissBoxValue.StartToEnd,
                                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                            }
                        )
                        val alignment = when (direction) {
                            SwipeToDismissBoxValue.StartToEnd -> Alignment.Center
                            SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                            SwipeToDismissBoxValue.Settled -> Alignment.Center
                        }
                        val icon = when (direction) {
                            SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Done
                            SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                            SwipeToDismissBoxValue.Settled -> Icons.Default.Done
                        }
                        val scale by animateFloatAsState(
                            if (dismissState.targetValue == SwipeToDismissBoxValue.Settled)
                                0.75f else 1f
                        )
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = alignment
                        ) {
                            Icon(
                                icon,
                                contentDescription = "Localized description",
                                modifier = Modifier.scale(scale)
                            )
                        }
                    }
                ) {
                    ShelfCard(
                        shelf,
                        navController,
                        { EditDialogState.value = true },
                        { onItemClick(shelf) })
                }
            }
        }
    }
}
