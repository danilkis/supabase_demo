package com.example.supabasedemo.qrcode

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.supabasedemo.R
import qrcode.QRCode
import java.io.File
import java.io.FileOutputStream


class QrWorker {
    fun logoQR(ctx: Context, path: String): Uri {
        val logo2 = ContextCompat.getDrawable(ctx.applicationContext, R.drawable.krizo)
        val logoBytes =
            ClassLoader.getSystemResourceAsStream(logo2.toString())?.readBytes() ?: ByteArray(0)

        val logoQRCode = QRCode.ofSquares()
            .withLogo(logoBytes, 150, 150)
            .build(path)
        val logoQRCodePngData = logoQRCode.renderToBytes()

        val fileName =
            ctx.getExternalFilesDir(null).toString() + "/" + System.currentTimeMillis() + ".png"
        val file = File(fileName)
        FileOutputStream(fileName).use { it.write(logoQRCodePngData) }
        val bmpUri = FileProvider.getUriForFile(ctx, "com.example.supabasedemo", file)
        Log.e("QR", "LOGO done")
        return bmpUri
    }
}


