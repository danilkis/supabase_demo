package com.example.supabasedemo.qrcode

import android.app.PendingIntent.getActivity
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.supabasedemo.R
import qrcode.QRCode
import qrcode.color.Colors
import java.io.FileOutputStream


class QrWorker {
    fun DemoQR(ctx: Context) {
        val helloWorld = QRCode.ofSquares()
            .withColor(Colors.BLACK) // Default is Colors.BLACK
            .withSize(10) // Default is 25
            .build("ShelfID:2 BoxID:3")

// By default, QRCodes are rendered as PNGs.
        val pngBytes = helloWorld.render()
        FileOutputStream("/sdcard/Download/hello-world.png").use { it.write(pngBytes.getBytes()) }
        Log.e("QR", "Done")
        this.logoQR(ctx)
    }

    fun logoQR(ctx: Context) { //Фото не работают
        val logo2 = ContextCompat.getDrawable(ctx.applicationContext, R.drawable.krizo)
        val logoBytes = ClassLoader.getSystemResourceAsStream("res/drawable/krizo.png")?.readBytes() ?: ByteArray(0)

        val logoQRCode = QRCode.ofSquares()
            .withLogo(logoBytes, 150, 150) // <- See Here --- Default: Will hide cells behind logo
            .build("New Demo QR")
        val logoQRCodePngData = logoQRCode.renderToBytes()

        // Simply add a logo image
        val transparentQRCode = QRCode.ofSquares()
            .withLogo(logoBytes, 150, 150, clearLogoArea = false) // <- See Here --- Keep cells behind logo
            .build("BOO!!! Happy Halloween!")
        val transparentQRCodePngData = transparentQRCode.renderToBytes()

        // ---------------------------
        // JVM-only code (saves the PNG Bytes to a file)
        FileOutputStream("/sdcard/Download/example03-logo.png").use { it.write(logoQRCodePngData) }
        FileOutputStream("/sdcard/Download/example03-logo-with-cells.png").use { it.write(transparentQRCodePngData) }
        Log.e("QR", "LOGO done")
    }
}

