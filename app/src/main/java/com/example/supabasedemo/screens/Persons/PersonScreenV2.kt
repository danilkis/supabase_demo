package com.example.supabasedemo.screens.Persons

import android.annotation.SuppressLint
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.customelements.Cards.ContactCard
import com.example.supabasedemo.customelements.Cards.ContactInfoHeader
import com.example.supabasedemo.customelements.Cards.LoadingCard
import com.example.supabasedemo.customelements.Cards.PersonCard
import com.example.supabasedemo.model.Persons.Contacts
import com.example.supabasedemo.model.Persons.Persons
import com.example.supabasedemo.screens.Persons.Dialogs.DeleteDialog
import com.example.supabasedemo.viewmodel.Person.PersonInfoViewmodel
import com.example.supabasedemo.viewmodel.Person.PersonInfoViewmodelFactory
import com.example.supabasedemo.viewmodel.Person.PersonsViewmodel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun twopane(navController: NavController, personsViewmodel: PersonsViewmodel) {
    // Currently selected item
    //var selectedItem: Persons? by rememberSaveable(stateSaver = Persons) { mutableStateOf(null) }
    var selectedItem: Persons by remember {
        mutableStateOf(Persons(0, "", "", 0))
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
            PersList(
                onItemClick = { id ->
                    // Set current item
                    selectedItem = id
                    // Display the detail pane
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                },
                navController = navController,
                viewModel = personsViewmodel
            )
        },
        detailPane = {
            // Show the detail pane content if selected item is available
            selectedItem.let { item ->
                PersInfo(person = item, PersonInfoViewmodel(selectedItem))
            }
        },
    )
}


@Composable
fun PersInfo(
    person: Persons,
    viewModel: PersonInfoViewmodel = viewModel {
        PersonInfoViewmodelFactory(person).create(PersonInfoViewmodel::class.java)
    }
) {
    Log.i("Pers", person.toString())
    val contact = viewModel.contacts.collectAsState(Contacts(0, "0", "0", "0")).value
    if (person.Name.isNullOrBlank() && person.Surname.isNullOrBlank()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Нажмите на карточку для информации")
        }
    }
    if (contact.id == 0 && contact.phone == "0") { //TODO: Crossfade
        ContactInfoHeader(person)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.contacts),
            style = MaterialTheme.typography.displaySmall
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ContactCard(contactName = "Phone", value = contact.phone)
            Spacer(modifier = Modifier.height(10.dp))
            LoadingCard()
            Spacer(modifier = Modifier.height(10.dp))
            LoadingCard()
            Spacer(modifier = Modifier.height(10.dp))
            LoadingCard()
            Spacer(modifier = Modifier.height(10.dp))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ContactInfoHeader(person = person)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.contacts),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                ContactCard(contactName = "Phone", value = contact.phone)
                Spacer(modifier = Modifier.height(10.dp))
                if (!contact.telegram.isNullOrBlank()) {
                    ContactCard(contactName = "Telegram", value = contact.telegram.toString())
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (!contact.url.isNullOrBlank()) {
                    ContactCard(contactName = "Url", value = contact.url.toString())
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersList( //TODO: Уменьшить кол-во рекомпозиций
    navController: NavController,
    viewModel: PersonsViewmodel = viewModel(),
    onItemClick: (Persons) -> Unit
) {
    val persons by viewModel.newPersons.collectAsState(initial = mutableListOf()) //TODO: Добавить skeleton loader
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
                    PersonCard(
                        Person = person,
                        navController = navController
                    ) { onItemClick(person) }
                }
            }
        }
    }
}