package com.example.supabasedemo.customelements

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.supabasedemo.R
import com.example.supabasedemo.qrcode.QrWorker

@Composable
fun GenerateQRButton(path: String) {
    val ctx: Context = LocalContext.current
    IconButton(
        onClick = { generateAndShare(ctx, path) },
        modifier = Modifier.size(75.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.QrCode,
            contentDescription = null,
            modifier = Modifier.size(75.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

fun generateAndShare(ctx: Context, path: String) {
    val uri = QrWorker().logoQR(ctx, path)

    val intent = Intent().apply {
        this.action = Intent.ACTION_SEND
        this.putExtra(Intent.EXTRA_STREAM, uri)
        this.type = "image/jpeg"
    }
    ctx.startActivity(Intent.createChooser(intent, ctx.getString(R.string.qr_save)))
}
