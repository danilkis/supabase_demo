package com.example.supabasedemo.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun Hello(navController: NavController) {
    navController.navigate("mainScreen") //TODO: Дописать
}