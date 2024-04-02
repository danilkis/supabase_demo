package com.example.supabasedemo.screens.Things

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.BoxInfoHeader
import com.example.supabasedemo.customelements.Cards.PersonCard
import com.example.supabasedemo.customelements.Cards.ThingCard
import com.example.supabasedemo.customelements.GenerateQRButton
import com.example.supabasedemo.customelements.ThingSheet
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Persons.Contacts
import com.example.supabasedemo.model.States
import com.example.supabasedemo.model.Things.Box
import com.example.supabasedemo.model.Things.Things
import com.example.supabasedemo.screens.Persons.Dialogs.DeleteDialog
import com.example.supabasedemo.screens.Persons.Info
import com.example.supabasedemo.screens.Persons.PersonLoaded
import com.example.supabasedemo.screens.Persons.PersonLoading
import com.example.supabasedemo.screens.Things.Dialogs.DeleteThingDialog
import com.example.supabasedemo.screens.Things.Dialogs.UpdateThingDialog
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxInfoScreen( //TODO: Если пусто показать надпись что пусто
    box: Box, navController: NavController, viewModel: ThingsViewmodel = viewModel()
) {
    Column(modifier = Modifier.fillMaxSize())
    {
        BoxInfoHeader(box = box)
        BoxInfoThingColumn(viewModel = viewModel, box = box, navController = navController)
         var currentState by remember { mutableStateOf(States.Empty) }
//        val things by viewModel.things.collectAsState(initial = mutableListOf())
//        Crossfade(targetState = currentState, animationSpec = tween(300, 100)) { state ->
//            when (state) {
//                States.Info -> Info()
//                States.Loading -> PersonLoading(person = person)
//                States.Loaded -> PersonLoaded(person = person, contact = contact)
//                States.Empty -> Box {}
//            }
//        }
//        if (things.size == 0) {
//            currentState = States.Info
//        } else {
//            if (contact.id == 0 && contact.phone == "0") {
//                currentState = States.Loading
//            } else {
//                currentState = States.Loaded
//            }
//        }
    }

}

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxInfoThingColumn(viewModel: ThingsViewmodel, box: Box, navController: NavController)
{
    var columnAppeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        columnAppeared = true
    }
    val things by viewModel.things.collectAsStateWithLifecycle()
    val types by viewModel.types.collectAsState(initial = mutableListOf())
    val ModalSheetState = remember { mutableStateOf(false) }
    val EditDialogState = remember { mutableStateOf(false) }
    var unread by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
    )
    {
        items(things.filter { it.boxId == box.id }) {  thing ->
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
                    ThingCard(thing, types, { ModalSheetState.value = true },
                        { EditDialogState.value = true })
                }
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