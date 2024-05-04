package com.example.supabasedemo.screens.Booting

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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@Composable
fun SignUp(navController: NavController) {
    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)

    var serverURL by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var signingUp by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // UI components for signing up
        Text(
            text = "Регистрация",
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
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text(text = stringResource(R.string.Password)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text(text = stringResource(R.string.PasswordRepeat)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Saving credentials and initiating sign-up
            sharedPreference.SaveString("Server_URL", serverURL)
            sharedPreference.SaveString("API_key", apiKey)
            sharedPreference.SaveString("Name", name)
            signingUp = true
        }) {
            Text(text = "Sign Up")
        }
        if (signingUp) {
            SignUpUser(navController, sharedPreference, { signingUp = false }, { signingUp = true })
        }
    }
}


@Composable
fun SignUpUser(
    navController: NavController,
    sharedPreference: SharedPreference,
    onFail: () -> Unit,
    onComplete: () -> Unit
) {
    // State variables
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Function to handle sign-in process
    fun signIn() {
        coroutineScope.launch {
            try {
                val serverURL = sharedPreference.GetString("Server_URL").toString()
                val apiKey = sharedPreference.GetString("API_key").toString()
                supaHelper.userSignUp(serverURL, apiKey)
                sharedPreference.SaveBool("Init_setup", true)
                onComplete()
                navController.navigate("OTPScreen")
            } catch (e: BadRequestRestException) {
                // Handling bad request exception
                showErrorDialog = true
                errorMessage = "Неверные учетные данные."
                sharedPreference.SaveBool("Init_setup", false)
            } catch (e: HttpRequestException) {
                // Handling HTTP request exception
                showErrorDialog = true
                errorMessage = "Нет подключения к серверу. Подождите немного или попробуйте вновь."
                sharedPreference.SaveBool("Init_setup", false)
            }
        }
    }
    // Execute sign-in process
    LaunchedEffect(key1 = Unit) {
        signIn()
    }

    @Composable
    // Function to create error dialog
    fun ErrorDialog() {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Ошибка") },
            text = { Text(text = errorMessage) },
            confirmButton = {
                Button(
                    onClick = {
                        showErrorDialog = false
                        onFail()
                    }
                ) {
                    Text(text = "OK")
                }
            }
        )
    }
    if (showErrorDialog) {
        ErrorDialog()
    }
    // Display error dialog if needed
}
