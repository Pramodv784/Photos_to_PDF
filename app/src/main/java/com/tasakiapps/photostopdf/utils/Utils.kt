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

     fun getImageDirectories(mContext: Context): ArrayList<String>? {
        val directories = ArrayList<String>()
        val contentResolver = mContext.contentResolver
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.DATA
        )
        val includeImages = MediaStore.Images.Media.MIME_TYPE + " LIKE 'image/%' "
        val excludeGif =
            " AND " + MediaStore.Images.Media.MIME_TYPE + " != 'image/gif' " + " AND " + MediaStore.Images.Media.MIME_TYPE + " != 'image/giff' "
        val selection = includeImages + excludeGif
        val cursor = contentResolver.query(queryUri, projection, selection, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val photoUri = cursor.getString(cursor.getColumnIndex(projection[0]))
                if (!directories.contains(File(photoUri).parent)) {
                    directories.add(File(photoUri).parent)
                }
            } while (cursor.moveToNext())
        }
        return directories
    }


}