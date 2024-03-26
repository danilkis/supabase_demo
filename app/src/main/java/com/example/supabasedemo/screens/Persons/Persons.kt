package com.example.supabasedemo.screens.Persons

import android.annotation.SuppressLint
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.PersonCard
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.screens.Persons.Dialogs.AddPersonDialog
import com.example.supabasedemo.screens.Persons.Dialogs.DeleteDialog
import com.example.supabasedemo.viewmodel.Person.PersonsViewmodel
import kotlinx.coroutines.launch

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
        val density = LocalDensity.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding(), start = 6.dp, end = 6.dp)
                .fillMaxWidth()
        ) {
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
                PersonColumn(navController, viewModel)
            }
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


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonColumn(navController: NavController, viewModel: PersonsViewmodel = viewModel()) {
    val persons by viewModel.newPersons.collectAsState(initial = mutableListOf())
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
        items(persons) { person ->
            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { distance -> distance * .25f }
            )
            val vibrator = LocalContext.current.getSystemService(Vibrator::class.java)
            val coroutineScope = rememberCoroutineScope()
            // check if the user swiped
            if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
// Create an array of timings in milliseconds
                DeleteDialog(
                    person,
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
                    PersonCard(Person = person, navController = navController)
            }
        }
    }
    }
}
