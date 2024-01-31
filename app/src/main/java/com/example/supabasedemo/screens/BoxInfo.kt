package com.example.supabasedemo.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.ThingCard
import com.example.supabasedemo.customelements.ThingSheet
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Box
import com.example.supabasedemo.screens.Things.Dialogs.DeleteThingDialog
import com.example.supabasedemo.screens.Things.Dialogs.UpdateThingDialog
import com.example.supabasedemo.viewmodel.ThingsViewmodel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxInfoScreen( //TODO: Если пусто показать надпись что пусто
    box: Box, navController: NavController, viewModel: ThingsViewmodel = viewModel()
) {
    Log.e("BoxInfo", box.name + "  " + box.id)
    val things by viewModel.things.collectAsState(initial = mutableListOf())
    val types by viewModel.types.collectAsState(initial = mutableListOf())
    val EditDialogState = remember { mutableStateOf(false) }
    var columnAppeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        columnAppeared = true
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserHead(
                id = box.id.toString(),
                firstName = box.name,
                lastName = " ",
                size = 80.dp,
                textStyle = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.padding(5.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    text = box.name,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = box.barcode.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.things),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)
        )
        {
            items(things.filter { it.boxId == box.id }) { thing ->
                val dismissState = rememberDismissState()
                val ModalSheetState = remember { mutableStateOf(false) }
                val coroutineScope = rememberCoroutineScope()
                val dismissDirection = dismissState.dismissDirection
                var isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
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
                                ThingSheet(thing = thing, types, { ModalSheetState.value = false }, navController)
                            }
                            if (EditDialogState.value)
                            {
                                UpdateThingDialog(open = EditDialogState.value, onDismiss = { EditDialogState.value = false }, thing = thing)
                            }
                        })
                }
            }
        }
    }
}