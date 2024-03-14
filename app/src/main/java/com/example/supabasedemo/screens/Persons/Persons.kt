package com.example.supabasedemo.screens.Persons

import android.annotation.SuppressLint
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
        if (persons.isEmpty()) {
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
                PersonColumn(navController, viewModel)
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
}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonColumn(navController: NavController, viewModel: PersonsViewmodel = viewModel()) {
    val persons by viewModel.newPersons.collectAsState(initial = mutableListOf())
    var columnAppeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        columnAppeared = true
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)
    )
    {
        items(persons) { person ->
            val dismissState = rememberDismissState()
            val vibrator = LocalContext.current.getSystemService(Vibrator::class.java)
            val coroutineScope = rememberCoroutineScope()
            val dismissDirection = dismissState.dismissDirection
            var isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            // check if the user swiped
            if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
// Create an array of timings in milliseconds
                DeleteDialog(
                    person,
                    viewModel,
                    onDismiss = {
                        isDismissed = true; coroutineScope.launch { dismissState.reset() }
                    },
                    onCancel = {
                        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
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
                    animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
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
                        PersonCard(person, navController = navController)
                    })
            }
        }
    }
}