package com.example.supabasedemo.screens.Things

import android.annotation.SuppressLint
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
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
import com.example.supabasedemo.screens.Things.Dialogs.AddBoxDialog
import com.example.supabasedemo.screens.Things.Dialogs.AddThingDialog
import com.example.supabasedemo.screens.Things.Dialogs.DeleteBoxDialog
import com.example.supabasedemo.screens.Things.Dialogs.DeleteThingDialog
import com.example.supabasedemo.screens.Things.Dialogs.UpdateThingDialog
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import kotlinx.coroutines.launch

//@RequiresApi(Build.VERSION_CODES.Q)
//@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3AdaptiveApi::class)
//@Composable
//fun ThingPanes(navController: NavController, thingsViewmodel: ThingsViewmodel) {
//    //var selec3tedItem: Things by rememberSaveable(stateSaver = HolderSaverThings) { mutableStateOf(Things(0,"","",0,0,"",0))  }
//    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
//
//    BackHandler(navigator.canNavigateBack()) {
//        navigator.navigateBack()
//    }
//
//    ListDetailPaneScaffold(
//        directive = navigator.scaffoldDirective,
//        value = navigator.scaffoldValue,
//        listPane = {
//            AnimatedPane {
//
//            }
//        },
//        detailPane = {
//            // Show the detail pane content if selected item is available
//            selectedItem.let { item ->
//                AnimatedPane(modifier = Modifier.preferredWidth(370.dp)) {
//
//                }
//            }
//        },
//    )
//}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ThingsMainScreen(navController: NavController, viewModel: ThingsViewmodel = viewModel()) {
    val things by viewModel.things.collectAsState(initial = mutableListOf())
    val boxes by viewModel.boxes.collectAsState(initial = mutableListOf())
    val openDialogThing = remember { mutableStateOf(false) }
    val openDialogBox = remember { mutableStateOf(false) }
    Scaffold(topBar = { SearchBarCustom(navController) }, floatingActionButton = {
        OptionsFAB({ openDialogBox.value = true }, { openDialogThing.value = true })
    })
    { paddingValues ->
        if (things.isEmpty() && boxes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Пока что тут пусто, добавьте что-нибудь")
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
                    { BoxColumn(navController = navController, viewModel, 4.dp) },
                    stringResource(
                        R.string.boxes
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                ToggleHeading(
                    { ThingColumn(navController = navController, viewModel, 4.dp) },
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
fun BoxColumn(
    navController: NavController,
    viewModel: ThingsViewmodel = viewModel(),
    paddingValues: Dp
) {
    val boxes by viewModel.boxes.collectAsState(initial = mutableListOf())
    var unread by remember { mutableStateOf(false) }
    var columnAppeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        columnAppeared = true
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues), verticalArrangement = Arrangement.spacedBy(4.dp)
    )
    {
        items(boxes) { box ->
            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { distance -> distance * .25f }
            )
            val vibrator = LocalContext.current.getSystemService(Vibrator::class.java)
            val coroutineScope = rememberCoroutineScope()
            // check if the user swiped
            if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
// Create an array of timings in milliseconds
                DeleteBoxDialog(
                    box,
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
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMedium)
                ) + scaleOut(
                    animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
                ),
                enter = fadeIn(
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMedium)
                ) + scaleIn(
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
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
                    BoxCard(box = box, navController = navController)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingColumn(
    navController: NavController,
    viewModel: ThingsViewmodel = viewModel(),
    paddingValues: Dp
) {
    val things by viewModel.things.collectAsState(initial = mutableListOf())
    val types by viewModel.types.collectAsState(initial = mutableListOf())
    val ModalSheetState = remember { mutableStateOf(false) }
    val EditDialogState = remember { mutableStateOf(false) }
    var unread by remember { mutableStateOf(false) }
    var columnAppeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        columnAppeared = true
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues), verticalArrangement = Arrangement.spacedBy(4.dp)
    )
    {
        items(things.filter { it.boxId.isNullOrBlank() }) { thing ->
            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { distance -> distance * .25f }
            )
            val vibrator = LocalContext.current.getSystemService(Vibrator::class.java)
            val coroutineScope = rememberCoroutineScope()
            // check if the user swiped
            if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
// Create an array of timings in milliseconds
                DeleteThingDialog(
                    thing,
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
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMedium)
                ) + scaleOut(
                    animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
                ),
                enter = fadeIn(
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMedium)
                ) + scaleIn(
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
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
                    ThingCard(
                        thing,
                        types,
                        { ModalSheetState.value = true },
                        { EditDialogState.value = true })
                }
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
            }
        }
    }
}


