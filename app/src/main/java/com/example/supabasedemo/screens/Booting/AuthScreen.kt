package com.example.supabasedemo.screens.Booting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
        SignInExistingUser(navController, sharedPreference, {}, {}, {})
    }
}

@Composable
fun AuthSetup(navController: NavController, sharedPreference: SharedPreference) {
    var serverURL by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var signingIn by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Логотип приложения
        Spacer(modifier = Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically)
        {
            Icon(
                painter = painterResource(R.drawable.logo), // Убедитесь, что у вас есть этот ресурс в папке drawable
                contentDescription = "",
                modifier = Modifier.size(128.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = "Workman",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.connect_to_server),
            style = MaterialTheme.typography.displayMedium
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
            label = { Text(text = stringResource(R.string.Password)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Сохранение учетных данных и инициирование входа в систему
            sharedPreference.SaveString("Server_URL", serverURL)
            sharedPreference.SaveString("API_key", apiKey)
            sharedPreference.SaveString("Name", name)
            signingIn = true
        }) {
            Text(text = stringResource(R.string.Sign_in))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            navController.navigate("Signup")
        }) {
            Text(text = stringResource(R.string.Signup))
        }
        if (signingIn) {
            SignInExistingUser(
                navController,
                sharedPreference,
                { signingIn = false; loading = false },
                { signingIn = true },
                { loading = true })
        }
    }
}

@Composable
fun SignInExistingUser(
    navController: NavController,
    sharedPreference: SharedPreference,
    onFail: () -> Unit,
    onComplete: () -> Unit,
    onLodaing: () -> Unit
) {
    val ctx = LocalContext.current
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val no_serv_conn = ctx.getString(R.string.No_serv_connection)
    val invalid_data = ctx.getString(R.string.WrongSignData)

    fun signIn() {
        coroutineScope.launch {
            try {
                val serverURL = sharedPreference.GetString("Server_URL").toString()
                val apiKey = sharedPreference.GetString("API_key").toString()
                supaHelper.userUUID = supaHelper.userSignIn(serverURL, apiKey).id
                onLodaing()
                navController.navigate("helloScreen")
                sharedPreference.SaveBool("Init_setup", true)
                onComplete()
            } catch (e: BadRequestRestException) {
                showErrorDialog = true
                errorMessage = invalid_data
                sharedPreference.SaveBool("Init_setup", false)
            } catch (e: HttpRequestException) {
                showErrorDialog = true
                errorMessage = no_serv_conn
                sharedPreference.SaveBool("Init_setup", false)
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        signIn()
    }

    if (loading) {
        CircularProgressIndicator()
    }
    @Composable
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
}