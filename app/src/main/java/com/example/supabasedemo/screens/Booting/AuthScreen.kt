package com.example.supabasedemo.screens.Booting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.SharedPreference
import com.example.supabasedemo.supabase.supaHelper

@Composable
fun Auth(navController: NavController) {
    val ctx = LocalContext.current
    val sharedPreference: SharedPreference = SharedPreference(ctx)
    if (!sharedPreference.GetBool("Init_setup")) {
        var serverURL by remember { mutableStateOf("") }
        var APIkey by remember { mutableStateOf("") }
        var Name by remember { mutableStateOf("") }
        var UserReady by remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.connect_to_server))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = Name,
                onValueChange = { Name = it },
                placeholder = { Text(text = stringResource(R.string.your_name)) }
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = serverURL,
                onValueChange = { serverURL = it },
                placeholder = { Text(text = stringResource(R.string.server_url)) }
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = APIkey,
                onValueChange = { APIkey = it },
                placeholder = { Text(text = "API key") }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = {
                sharedPreference.SaveString("Server_URL", serverURL)
                sharedPreference.SaveBool("Init_setup", true)
                sharedPreference.SaveString("API_key", APIkey)
                sharedPreference.SaveString("Name", Name)
                UserReady = true
            }) {
                Text(text = stringResource(R.string.done))
            }
            if (UserReady) {
                LaunchedEffect(true)
                {
                    navController.navigate("helloScreen")
                }
            }
        }
    } else {
        supaHelper.url = sharedPreference.GetString("Server_URL").toString()
        supaHelper.key = sharedPreference.GetString("API_key").toString()
        LaunchedEffect(true)
        {
            navController.navigate("helloScreen")
        }
    }
}