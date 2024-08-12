package com.tasakiapps.photostopdf.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.tasakiapps.photostopdf.model.FileInfo
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

object ImageUtil {

     fun convertUrlToPath(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val contentResolver: ContentResolver = context.contentResolver

        // Check if the scheme is "file" (local file)
        if (uri.scheme == "file") {
            filePath = uri.path
        } else if (uri.scheme == "content") {
            // For "content" scheme, query the MediaStore for the file path
            filePath = getFilePathFromMediaStore(context, uri)
        }

        return filePath
    }
    private fun getFilePathFromMediaStore(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            filePath = cursor.getString(columnIndex)
        }

        return filePath
    }

    fun getFileInfo(filePath: String) :FileInfo {
        val file = File(filePath)


        if (file.exists()) {
            // Get file name
            val fileName = file.name
            println("File Name: $fileName")

            // Get absolute path
            val absolutePath = file.absolutePath
            println("Absolute Path: $absolutePath")

            // Get file size in bytes
            val fileSize = file.length()
            println("File Size: $fileSize bytes")

            // Get last modified timestamp
            val lastModified = file.lastModified()
            println("Last Modified: $lastModified")

            // Check if it's a directory
            val isDirectory = file.isDirectory
            println("Is Directory: $isDirectory")
             return FileInfo(fileName,getStringSizeLengthFile(fileSize), convertLongToTime(lastModified),absolutePath)
        } else {
            return FileInfo("","","","")
            println("File not found")
        }
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd")
        return format.format(date)
    }
    fun getStringSizeLengthFile(size: Long): String {
        val df = DecimalFormat("0.00")
        val sizeKb = 1024.0f
        val sizeMb = sizeKb * sizeKb
        val sizeGb = sizeMb * sizeKb
        val sizeTerra = sizeGb * sizeKb
        if (size < sizeMb) return df.format(size / sizeKb) + " Kb" else if (size < sizeGb) return df.format(
            size / sizeMb
        ) + " Mb" else if (size < sizeTerra) return df.format(size / sizeGb) + " Gb"
        return ""
    }

    fun <T> MutableList<T>.removeIfCompat(predicate: ((T) -> Boolean)): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            removeIf { predicate.invoke(it) }
        } else {
            var removed = false
            val each = iterator()
            while (each.hasNext()) {
                if (predicate.invoke(each.next())) {
                    each.remove()
                    removed = true
                }
            }
            removed
        }
    }

}