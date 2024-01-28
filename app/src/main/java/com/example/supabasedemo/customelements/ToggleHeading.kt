package com.example.supabasedemo.customelements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Composable
fun ToggleHeading(Content: @Composable () -> Unit, heading: String) {
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(if (expanded) 180f else 0f)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = heading, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Expand or collapse",
                modifier = Modifier.rotate(rotationAngle)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        AnimatedVisibility(
            visible = expanded,
            exit = fadeOut(
                animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMedium)
            ) + scaleOut(
                animationSpec =  spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
            ),
            enter = fadeIn(
                animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMedium)
            ) + scaleIn(
                animationSpec =  spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
            )
        ) {
            Content()
        }
    }
}
