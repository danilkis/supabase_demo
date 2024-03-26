package com.example.supabasedemo.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AllInbox
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.supabasedemo.R
import com.example.supabasedemo.model.MainScreenDest
import com.example.supabasedemo.screens.Persons.PersonScreen
import com.example.supabasedemo.screens.Shelf.ShelfScreen
import com.example.supabasedemo.screens.Things.ThingsMainScreen


@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visibleState = MutableTransitionState(
            initialState = false
        ).apply { targetState = true },
        modifier = Modifier,
        enter = scaleIn(animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessHigh)),
        exit = scaleOut(animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessHigh)),
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3AdaptiveNavigationSuiteApi::class)
@Composable
fun MainScreenNavigation(navControllerGeneral: NavHostController) { //

    val destinations = listOf(
        MainScreenDest(stringResource(id = R.string.persons), Icons.Rounded.Person) {
            EnterAnimation {
                PersonScreen(it)
            }
        },
        MainScreenDest(stringResource(R.string.things), Icons.Rounded.Build) {
            EnterAnimation {
                ThingsMainScreen(it)
            }
        },
        MainScreenDest(stringResource(R.string.shelves), Icons.Rounded.AllInbox) {
            EnterAnimation {
                ShelfScreen(it)
            }
        },
    )
    val selected = remember { mutableIntStateOf(0) }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            destinations.forEachIndexed { index, (name, icon) ->
                this.item(
                    icon = { Icon(imageVector = icon, contentDescription = null) },
                    label = { Text(text = name) },
                    selected = selected.intValue == index,
                    onClick = { selected.intValue = index }
                )
            }
        },
    ) {
        Crossfade(targetState = selected.intValue, label = "CurrentPage") {
            destinations[it].content(navControllerGeneral)
        }
    }
}