package com.example.supabasedemo.screens.Shelf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.Cards.LoadingCard
import com.example.supabasedemo.model.Shelf.Shelf

@Composable
fun ShelfLoading(shelf: Shelf, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        ShelfInfoHeader(shelf = shelf, navController = navController)
        Spacer(modifier = Modifier.height(10.dp))
        LoadingCard()
        Spacer(modifier = Modifier.height(10.dp))
        LoadingCard()
        Spacer(modifier = Modifier.height(10.dp))
        LoadingCard()
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun ShelfLoaded(shelf: Shelf, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {
        ShelfInfoScreen(shelf = shelf, navController)
    }
}