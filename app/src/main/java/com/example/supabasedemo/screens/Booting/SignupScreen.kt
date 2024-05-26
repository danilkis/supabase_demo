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
import java.util.regex.Pattern

@Composable
fun SignUp(navController: NavController) {
    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)

    var serverURL by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    var passwordRep by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var signingUp by remember { mutableStateOf(false) }
    var dataError by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordVisible1 by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        fun isValidEmail(email: String): Boolean {
            val emailPattern = Pattern.compile(
                "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            )
            return emailPattern.matcher(email).matches()
        }
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

        Text(
            text = stringResource(id = R.string.Signup),
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
            onValueChange = {
                serverURL = it
                emailError = !isValidEmail(it)
            },
            isError = emailError,
            label = { Text(text = stringResource(R.string.Login)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = apiKey,
            isError = passwordError,
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
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = passwordRep,
            isError = passwordError,
            onValueChange = {
                passwordRep = it
                passwordError = it != apiKey
            },
            label = { Text(text = stringResource(R.string.PasswordRepeat)) },
            visualTransformation = if (passwordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible1)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible1) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible1 = !passwordVisible1 }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {

        // Saving credentials and initiating sign-up
            sharedPreference.SaveString("Server_URL", serverURL)
            sharedPreference.SaveString("API_key", apiKey)
            sharedPreference.SaveString("Name", name)
            if (passwordRep == apiKey && !emailError) {
                signingUp = true
            } else {
                dataError = true
            }

        }) {
            Text(text = "Sign Up")
        }
        if (signingUp) {
            SignUpUser(navController, sharedPreference, { signingUp = false }, { signingUp = true })
        }
        @Composable
        fun ErrorDialog() {
            AlertDialog(
                onDismissRequest = { dataError = false },
                title = { Text(text = stringResource(R.string.Error)) },
                text = { Text(text = stringResource(R.string.WrongSignData)) },
                confirmButton = {
                    Button(
                        onClick = {
                            dataError = false
                        }
                    ) {
                        Text(text = "OK")
                    }
                }
            )
        }
        if (dataError) {
            ErrorDialog()
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
