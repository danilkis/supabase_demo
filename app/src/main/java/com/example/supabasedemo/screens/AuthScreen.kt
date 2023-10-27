package com.example.supabasedemo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.SharedPreference
import com.example.supabasedemo.supa.supaHelper
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun Auth(navController: NavController) {
    var ctx = LocalContext.current
    val sharedPreference: SharedPreference =SharedPreference(ctx)
    if (!sharedPreference.GetBool("Init_setup"))
    {
        var serverURL by remember { mutableStateOf("") }
        var APIkey by remember { mutableStateOf("") }
        var Name by remember { mutableStateOf("") }
        var UserReady by remember {
            mutableStateOf(false)
        }
    Column(modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Time to set up your server")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = Name,
            onValueChange = { Name = it },
            placeholder = { Text(text = "Your name") }
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = serverURL,
            onValueChange = { serverURL = it },
            placeholder = { Text(text = "Server URL") }
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = APIkey,
            onValueChange = { APIkey = it },
            placeholder = { Text(text = "API key") }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = {
                sharedPreference.SaveString("Server_URL", serverURL.toString())
                sharedPreference.SaveBool("Init_setup", true)
                sharedPreference.SaveString("API_key", APIkey.toString())
                sharedPreference.SaveString("Name", Name.toString())
                UserReady = true
            }) {
            Text(text = "Done")
        }
        if (UserReady)
        {
            LaunchedEffect(true)
            {
                navController.navigate("helloScreen")
            }
        }
    }
    }
    else
    {
        supaHelper.url = sharedPreference.GetString("Server_URL").toString()
        supaHelper.key = sharedPreference.GetString("API_key").toString()
        LaunchedEffect(true)
        {
            navController.navigate("helloScreen")
        }
    }
}