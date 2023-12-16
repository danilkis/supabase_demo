package com.example.supabasedemo.customelements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.supabasedemo.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OptionsFAB(OnBoxOpen:() -> Unit, OnThingsOpen:() -> Unit) {
    var myFABState by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.End) {
        AnimatedVisibility(
            visible = myFABState,
            exit = fadeOut(
                animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
            ) + slideOutVertically (
                targetOffsetY  = { fullHeight -> fullHeight },
                animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
            ),
            enter = fadeIn(
                animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
            ) + slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
            )
        ) {
            Column(horizontalAlignment = Alignment.End)
            {
                ExtendedFloatingActionButton(
                    text = { Text(text = stringResource(R.string.add_thing_fab)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Sell,
                            contentDescription = null
                        )
                    },
                    onClick = { OnThingsOpen() }
                )
                Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
                ExtendedFloatingActionButton(
                    text = { Text(text = stringResource(R.string.add_box_fab)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Inbox,
                            contentDescription = null
                        )
                    },
                    onClick = { OnBoxOpen() }
                )
                Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
            }
        }
        ExtendedFloatingActionButton(
            text = { Text(text = stringResource(R.string.add)) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null
                )
            },
            onClick = { myFABState = !myFABState },
            expanded = myFABState
        )
    }
}