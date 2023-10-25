package com.example.supabasedemo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.supabasedemo.SharedPreference
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@Composable
fun Hello(navController: NavController) {
    Column() {
        val sharedPreference: SharedPreference = SharedPreference(LocalContext.current)
        var name = sharedPreference.GetString("Name")
        Text(text = name.toString())
    }
    navController.navigate("mainScreen") //TODO: Дописать
}