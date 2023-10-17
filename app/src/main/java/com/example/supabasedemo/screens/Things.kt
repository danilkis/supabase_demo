package com.example.supabasedemo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.supabasedemo.customelements.SearchBarCustom
import com.example.supabasedemo.viewmodel.PersonsViewmodel

@Composable
fun ThingsMainScreen(navController: NavController, viewModel: PersonsViewmodel = viewModel())
{
    Column {
        SearchBarCustom()
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Things",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
    }
}