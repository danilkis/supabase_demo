package com.example.supabasedemo.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

data class MainScreenDest(
    val name: String,
    val icon: ImageVector,
    val content: @Composable ((navController: NavController) -> Unit)
)
