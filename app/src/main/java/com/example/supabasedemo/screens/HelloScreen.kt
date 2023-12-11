package com.example.supabasedemo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.SharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun Hello(navController: NavController) {
    val nameVis by remember { mutableStateOf(true) }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
        var userReady by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(null) {
            withContext(Dispatchers.IO) {
                delay(1000)
                userReady = true
            }

        }
        val sharedPreference = SharedPreference(LocalContext.current)
        val name = sharedPreference.GetString("Name")
        if(!userReady)
        {
            AnimatedVisibility(
                nameVis, enter = expandHorizontally(
                    animationSpec = tween(2000)
                ), exit = shrinkHorizontally(animationSpec = tween(2000)),
                modifier = Modifier.padding(top = 10.dp)
            ) {
            Text(text = stringResource(R.string.welcome_back, name.toString()) , style = MaterialTheme.typography.bodyLarge)
            }
        }
        else
        {
            LaunchedEffect(true)
            {
                navController.navigate("mainScreen")
            }
        }
    }
}