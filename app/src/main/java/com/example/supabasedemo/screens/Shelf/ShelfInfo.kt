package com.example.supabasedemo.screens.Shelf

import android.annotation.SuppressLint
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.BoxSheet
import com.example.supabasedemo.customelements.Cards.BoxCard
import com.example.supabasedemo.customelements.GenerateQRButton
import com.example.supabasedemo.customelements.UserHead
import com.example.supabasedemo.model.Shelf.Shelf
import com.example.supabasedemo.model.Things.Box
import com.example.supabasedemo.screens.Shelf.Dialog.UnlinkBoxDialog
import com.example.supabasedemo.viewmodel.Shelf.ShelfBoxesViewmodel
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShelfInfoScreen(
    shelf: Shelf,
    navController: NavController,
    viewModel: ShelfBoxesViewmodel = viewModel(),
    viewModel2: ThingsViewmodel = viewModel()
) {
    Scaffold(topBar = { ShelfInfoHeader(shelf = shelf, navController) }) {
        var unread by remember { mutableStateOf(false) }
        var columnAppeared by remember { mutableStateOf(false) }
        val boxes by viewModel2.boxes.collectAsStateWithLifecycle(initialValue = listOf())
        val shelfBoxes by viewModel.shelves_boxes.collectAsState()

        Log.i("ShelfInfoScreem", shelfBoxes.toString())

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 210.dp, start = 6.dp, end = 6.dp)
        ) {
            items(shelfBoxes.flatMap { shelf -> boxes.filter { shelf.BoxID == it.id } }) {
                val dismissState = rememberSwipeToDismissBoxState(
                    positionalThreshold = { distance -> distance * .25f }
                )
                val vibrator = LocalContext.current.getSystemService(Vibrator::class.java)
                val coroutineScope = rememberCoroutineScope()
                // check if the user swiped
                if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                    UnlinkBoxDialog(
                        shelf,
                        it,
                        viewModel,
                        { unread = false; coroutineScope.launch { dismissState.reset() } },
                        {
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
                        state = dismissState, // Ограничение свайпа справа налево
                        backgroundContent = {
                            val direction = dismissState.dismissDirection
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.background
                                    SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.secondaryContainer
                                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                                }
                            )
                            val alignment = when (direction) {
                                SwipeToDismissBoxValue.StartToEnd -> Alignment.Center
                                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                                SwipeToDismissBoxValue.Settled -> Alignment.Center
                            }
                            val icon = when (direction) {
                                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Edit
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
                        BoxCard(it, navController, { navController.navigate("box/${it.id}") })
                    }
                }
            }
        }
    }
}
/*

        Spacer(modifier = Modifier.height(.dp))

 */


@Composable
fun ShelfInfoHeader(
    shelf: Shelf,
    navController: NavController,
    viewModel: ShelfBoxesViewmodel = viewModel()
) {
    var openSheet = remember { mutableStateOf(false) }
    var boxChosen = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var box by remember { mutableStateOf(Box("0", "0", "0")) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserHead(
                id = shelf.id,
                firstName = shelf.name,
                lastName = shelf.id,
                size = 95.dp,
                textStyle = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    text = shelf.name,
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.shelf_info_room) + shelf.room,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = stringResource(R.string.shelf_info_floor) + shelf.floor.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Row(
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.End
            )
            {
                GenerateQRButton(path = "S" + shelf.id)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.Center)
        {
            OutlinedButton(onClick = { openSheet.value = true }) {
                Text(stringResource(R.string.add_box))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (openSheet.value)
        {
            BoxSheet(
                onDismiss = { openSheet.value = false },
                navController = navController,
                onChosen = { box = it; boxChosen.value = true; openSheet.value = false })
        }
        LaunchedEffect(boxChosen.value) {
            if (boxChosen.value) {
                coroutineScope.launch {
                    viewModel.addBoxToShelf(shelf, box)
                }
            }
        }
    }
}