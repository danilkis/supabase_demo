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
fun Auth(navController: NavController) {
    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)

    if (!sharedPreference.GetBool("Init_setup")) {
        AuthSetup(navController, sharedPreference)
    } else {
        SignInExistingUser(navController, sharedPreference, {}, {})
    }
}

@Composable
fun AuthSetup(navController: NavController, sharedPreference: SharedPreference) {
    var serverURL by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var signingIn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // UI components for setting up authentication
        // Omitted for brevity
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
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text(text = stringResource(R.string.Password)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Saving credentials and initiating sign-in
            sharedPreference.SaveString("Server_URL", serverURL)
            sharedPreference.SaveString("API_key", apiKey)
            sharedPreference.SaveString("Name", name)
            signingIn = true
        }) {
            Text(text = stringResource(R.string.Sign_in))
        }
        Button(onClick = {
            navController.navigate("Signup")
        }) {
            Text(text = stringResource(R.string.Signup))
        }
        if (signingIn) {
            SignInExistingUser(
                navController,
                sharedPreference,
                { signingIn = false },
                { signingIn = true })
        }
    }
}

@Composable
fun SignInExistingUser(
    navController: NavController,
    sharedPreference: SharedPreference,
    onFail: () -> Unit,
    onComplete: () -> Unit
) {
    val ctx = LocalContext.current
    // State variables
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val no_serv_conn = ctx.getString(R.string.No_serv_connection)
    val invalid_data = ctx.getString(R.string.WrongSignData)
    // Function to handle sign-in process
    fun signIn() {
        coroutineScope.launch {
            try {
                val serverURL = sharedPreference.GetString("Server_URL").toString()
                val apiKey = sharedPreference.GetString("API_key").toString()
                supaHelper.userUUID = supaHelper.userSignIn(serverURL, apiKey).id
                navController.navigate("helloScreen")
                sharedPreference.SaveBool("Init_setup", true)
                onComplete()
            } catch (e: BadRequestRestException) {
                // Handling bad request exception
                showErrorDialog = true
                errorMessage = invalid_data
                sharedPreference.SaveBool("Init_setup", false)
            } catch (e: HttpRequestException) {
                // Handling HTTP request exception
                showErrorDialog = true
                errorMessage = no_serv_conn
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
            title = { Text(text = stringResource(R.string.Error)) },
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

