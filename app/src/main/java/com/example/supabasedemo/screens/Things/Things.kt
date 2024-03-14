package com.example.supabasedemo.screens.Things

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.Cards.BoxCard
import com.example.supabasedemo.customelements.Cards.ThingCard
import com.example.supabasedemo.customelements.OptionsFAB
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.customelements.ThingSheet
import com.example.supabasedemo.customelements.ToggleHeading
import com.example.supabasedemo.screens.Things.Dialogs.*
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ThingsMainScreen(navController: NavController, viewModel: ThingsViewmodel = viewModel()) {
    val things by viewModel.things.collectAsState(initial = mutableListOf())
    val openDialogThing = remember { mutableStateOf(false) }
    val openDialogBox = remember { mutableStateOf(false) }
    Scaffold(topBar = { SearchBarCustom(navController) }, floatingActionButton = {
        OptionsFAB({ openDialogBox.value = true }, { openDialogThing.value = true })
    })
    { paddingValues ->
        if (things.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
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
                ToggleHeading(
                    { BoxColumn(navController = navController, viewModel) },
                    stringResource(
                        R.string.boxes
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                ToggleHeading(
                    { ThingColumn(navController = navController, viewModel) },
                    stringResource(
                        R.string.unsorted
                    )
                )
            }
        }
        if (openDialogThing.value) {
            AddThingDialog(
                openDialogThing.value,
                onDismiss = { openDialogThing.value = false },
                viewModel
            )
        }
        if (openDialogBox.value) {
            AddBoxDialog(
                openDialogBox.value,
                onDismiss = { openDialogBox.value = false },
                viewModel
            )
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
        items(boxes.filter { it.id != 1 }) { box ->
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


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") //TODO: Сделать в удобную функцию
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
                        ThingCard(
                            thing,
                            types,
                            { ModalSheetState.value = true },
                            { EditDialogState.value = true })
                        if (ModalSheetState.value) {
                            ThingSheet(
                                thing = thing,
                                types,
                                { ModalSheetState.value = false },
                                navController
                            )
                        }
                        if (EditDialogState.value) {
                            UpdateThingDialog(
                                open = EditDialogState.value,
                                onDismiss = { EditDialogState.value = false },
                                thing = thing
                            )
                        }
                    })
            }
        }
    }
}