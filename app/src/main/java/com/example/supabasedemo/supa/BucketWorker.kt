package com.example.supabasedemo.supa

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class BucketWorker {
    fun UploadFile(file_path: String, contentResolver: ContentResolver, ctx: Context): String {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val client = supaHelper.getAsyncClient()
                val bucket = client.storage["photos"]

                // Get the file URI from the file path
                val uri = Uri.parse(file_path)

                // Use contentResolver to open an input stream to read the file bytes
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    // Read the file bytes from the input stream
                    val fileBytes = inputStream.readBytes()
                    // Create a temporary file from the file byte
                    bucket.upload(
                        "${file_path.substring(file_path.lastIndexOf("/") + 1)}.jpg",
                        fileBytes,
                        upsert = false
                    )
                    bucket.createSignedUrl(
                        path = "${file_path.substring(file_path.lastIndexOf("/") + 1)}.jpg",
                        expiresIn = 52590000.minutes
                    )
                }
            } catch (e: Exception) {
                Log.e("BUCKET", e.toString())
            }
        }
        val url = supaHelper.client.storage["photos"].publicUrl(
            "${
                file_path.substring(
                    file_path.lastIndexOf("/") + 1
                )
            }.jpg"
        )
        Log.e("BUCKET", url)
        return url
    }
}
