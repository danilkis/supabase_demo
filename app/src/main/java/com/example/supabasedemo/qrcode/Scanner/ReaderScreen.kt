package com.example.supabasedemo.qrcode.Scanner

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supabasedemo.viewmodel.Things.ThingsViewmodel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReaderScreen(navController: NavController, thingsVM: ThingsViewmodel) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        FeatureThatRequiresCameraPermission(deniedContent = { status ->
            NeedCameraPermissionScreen(
                requestPermission = cameraPermissionState::launchPermissionRequest,
                shouldShowRationale = status.shouldShowRationale
            )
        },
            grantedContent = {
                Scaffold { paddingValues ->
                    CameraPreview(navController, thingsVM)
                }
            }
        )
    }
}