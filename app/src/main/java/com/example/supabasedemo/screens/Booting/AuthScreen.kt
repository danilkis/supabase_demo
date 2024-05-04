package com.example.supabasedemo.screens.Booting

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.R
import com.example.supabasedemo.SharedPreference
import com.example.supabasedemo.supabase.supaHelper
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException

@Composable
fun Auth(navController: NavController) {
    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)

    if (!sharedPreference.GetBool("Init_setup")) {
        AuthSetup(navController, sharedPreference)
    } else {
        SignInExistingUser(navController, sharedPreference)
    }
}

@Composable
fun AuthSetup(navController: NavController, sharedPreference: SharedPreference) {
    var serverURL by remember { mutableStateOf("") }
    var APIKey by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var SignIn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.connect_to_server),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = stringResource(R.string.your_name)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = serverURL,
            onValueChange = { serverURL = it },
            label = { Text(text = stringResource(R.string.Login)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = APIKey,
            onValueChange = { APIKey = it },
            label = { Text(text = stringResource(R.string.Password)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            sharedPreference.SaveString("Server_URL", serverURL)
            sharedPreference.SaveString("API_key", APIKey)
            sharedPreference.SaveString("Name", name)
            // Navigate to the next screen
            SignIn = true
        }) {
            Text(text = "Войти")
        }
        if (SignIn) {
            SignInExistingUser(navController = navController, sharedPreference = sharedPreference)
            SignIn = false
        }
    }
}

@Composable
fun SignInExistingUser(navController: NavController, sharedPreference: SharedPreference) {
    var showErrorLoginDialog by remember { mutableStateOf(false) }
    var showErrorConnectionDialog by remember { mutableStateOf(false) }
    var userReady by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        try {
            val serverURL = sharedPreference.GetString("Server_URL").toString()
            val APIKey = sharedPreference.GetString("API_key").toString()
            supaHelper.userUUID = supaHelper.userSignIn(serverURL, APIKey).id
            navController.navigate("helloScreen")
            sharedPreference.SaveBool("Init_setup", true)
        } catch (e: BadRequestRestException) {
            showErrorLoginDialog = true
            Log.e("Login", e.toString())
            sharedPreference.SaveBool("Init_setup", false)
        } catch (e: HttpRequestException) {
            Log.e("Login", e.toString())
            showErrorConnectionDialog = true
            sharedPreference.SaveBool("Init_setup", false)
        }

    }

    if (showErrorLoginDialog) {
        // Show dialog for incorrect credentials
        AlertDialog(
            onDismissRequest = { showErrorLoginDialog = false },
            title = { Text(text = "Ошибка") },
            text = { Text(text = "Неверные учетные данные. Пожалуйста, проверьте свой URL сервера и API ключ.") },
            confirmButton = {
                Button(
                    onClick = {
                        showErrorLoginDialog = false
                        // Clear stored credentials on dismissal
                        //sharedPreference.ClearAll()
                    }
                ) {
                    Text(text = "OK")
                }
            }
        )
    }

    if (showErrorConnectionDialog) {
        // Show dialog for incorrect credentials
        AlertDialog(
            onDismissRequest = { showErrorLoginDialog = false },
            title = { Text(text = "Ошибка") },
            text = { Text(text = "Нету подкючения к серверу. Подождите немного или попробуйте вновь.") },
            confirmButton = {
                Button(
                    onClick = {
                        showErrorLoginDialog = false
                        // Clear stored credentials on dismissal
                        //sharedPreference.ClearAll()
                    }
                ) {
                    Text(text = "Попробовать снова")
                }
            }
        )
    }
}
