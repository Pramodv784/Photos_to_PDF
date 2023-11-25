package com.tasakiapps.photostopdf.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import com.tasakiapps.photostopdf.model.GridViewItem
import java.io.File
import java.net.URI
import java.util.ArrayList

object Utils {

    fun getPdfPathFromUri(context: Context, uri: Uri): String? {
        var filePath: String? = null

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val contentResolver: ContentResolver = context.contentResolver

        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = cursor.getString(columnIndex)
            }
        }

        return filePath
    }

    fun getFileUri(context: Context, fileName: String?): Uri? {
        // Get the file from the external storage directory
        val file = File(context.getExternalFilesDir(null), fileName)

        // Generate a content URI using FileProvider
        return FileProvider.getUriForFile(
            context,
            "com.tasakiapps.photostopdf.fileprovider",  // Make sure to replace with your app's package name
            file
        )
    }

    @WorkerThread
    fun Context.retrievePhotos(): List<GridViewItem> {
        val results = mutableListOf<GridViewItem>()
        val resolver = contentResolver
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
        )

        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        )
        if (cursor != null) {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)

                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                //do whatever you need with the uri
                //TODO disable check corrupt image for client testing.
                // Reason: slow load image from gallery
//            if (uri.size(this) > 0) {
                results.add(GridViewItem("", File(uri.toString()).path, 0))
//            }
            }
        }

        cursor?.close()
        return results
    }


}