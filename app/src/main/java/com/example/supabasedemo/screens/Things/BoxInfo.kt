package com.example.supabasedemo.screens.Things

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.GenerateQRButton
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Things.Box
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel


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
    Column(Modifier.padding(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserHead(
                id = box.id.toString(),
                firstName = box.name,
                lastName = box.id.toString(),
                size = 80.dp,
                textStyle = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier.padding(10.dp),
                text = box.name,
                style = MaterialTheme.typography.headlineLarge
            )
            Row(
                horizontalArrangement = Arrangement.Absolute.Right,
                modifier = Modifier.fillMaxWidth()
            )
            {
                GenerateQRButton(path = "B" + box.id)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.things),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}
/*
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
                ) { Text(text = "AAA") } }}}}
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

 */