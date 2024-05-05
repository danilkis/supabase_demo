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
import com.example.supabasedemo.model.Things.Box
import com.example.supabasedemo.supabase.supaHelper
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import kotlinx.coroutines.launch

@Composable
fun OTPScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)
    var OTPToken by remember { mutableStateOf("") }
    var OTP by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // UI components for signing up
        Text(
            text = stringResource(R.string.OTP_Mail_message),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = OTPToken,
            onValueChange = { OTPToken = it },
            label = { Text(text = "OTP") }
        )
        Button(onClick = {
            // Saving credentials and initiating sign-up
            OTP = true
        }) {
            Text(text = stringResource(id = R.string.done))
        }
        if (OTP) {
            OTPdone(
                navController,
                sharedPreference,
                { OTP = false },
                { OTP = true },
                OTPToken.toString()
            )
        }
    }
}


@Composable
fun OTPdone(
    navController: NavController,
    sharedPreference: SharedPreference,
    onFail: () -> Unit,
    onComplete: () -> Unit,
    Token: String
) {
    // State variables
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val ctx = LocalContext.current
    val no_serv_conn = ctx.getString(R.string.No_serv_connection)
    val invalid_data = ctx.getString(R.string.WrongSignData)
    val coroutineScope = rememberCoroutineScope()
    val unsorted = stringResource(id = R.string.unsorted)

    // Function to handle sign-in process
    fun OTPCheck() {
        coroutineScope.launch {
            try {
                val serverURL = sharedPreference.GetString("Server_URL").toString()
                supaHelper.userUUID = supaHelper.verifyOTP(serverURL, Token).id
                ThingsViewmodel().insertBoxes(Box("", unsorted, supaHelper.userUUID))
                sharedPreference.SaveBool("Init_setup", true)
                onComplete()
                navController.navigate("helloScreen")
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
        OTPCheck()
    }

    @Composable
    // Function to create error dialog
    fun ErrorDialog() {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = stringResource(id = R.string.Error)) },
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