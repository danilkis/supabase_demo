package com.example.supabasedemo.supa

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import io.github.jan.supabase.storage.UploadData
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class BucketWorker {
    fun UploadFile(file_path: String, contentResolver: ContentResolver) {
        CoroutineScope(Dispatchers.Main).launch {
            val client = supaHelper.getAsyncClient()
            val bucket = client.storage["photos"]

            // Get the file URI from the file path
            val uri = Uri.parse(file_path)

            // Use contentResolver to open an input stream to read the file bytes
            contentResolver.openInputStream(uri)?.use { inputStream ->
                // Read the file bytes from the input stream
                val fileBytes = inputStream.readBytes()
                // Upload the file to the bucket
                bucket.upload("${file_path.substring(file_path.lastIndexOf("/") + 1)}.jpg", fileBytes, upsert = false)
            }
        }
    }
}
