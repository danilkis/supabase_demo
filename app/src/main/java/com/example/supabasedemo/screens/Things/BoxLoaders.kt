package com.example.supabasedemo.screens.Things

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.supabasedemo.customelements.BoxInfoHeader
import com.example.supabasedemo.customelements.Cards.LoadingCard
import com.example.supabasedemo.model.Things.Box

@Composable
fun BoxLoading(
    box: Box,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        BoxInfoHeader(box = box)
        LoadingCard()
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
fun BoxLoaded(box: Box, navController: NavController, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        BoxInfoHeader(box = box)
        BoxInfoThingColumn(box, navController)
    }
}