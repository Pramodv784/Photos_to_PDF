package com.tasakiapps.photostopdf.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.model.GridViewItem
import java.io.File
import java.net.URI
import java.util.ArrayList

object Utils {

    private lateinit var dialog: AlertDialog

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

    fun getAlertDialog(
        context: Activity,
        layout: Int,
        setCancellationOnTouchOutside: Boolean,
        message: String
    ): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val customLayout: View =
            context.layoutInflater.inflate(layout, null)
        builder.setView(customLayout)
        var textView = customLayout.findViewById<TextView>(R.id.text_progress_bar)
         textView.text = message
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(setCancellationOnTouchOutside)
        return dialog
    }

    fun showProgressDialog(context: Activity, message: String): AlertDialog {
         dialog = getAlertDialog(context, R.layout.progress_loader,
            setCancellationOnTouchOutside = false,message)
        dialog.show()
        return dialog
    }

    fun dismissProgressDialog(){
       if(dialog.isShowing) dialog.dismiss()
    }

    fun filePathToBitmap(filePath: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun convertToPortrait(bitmap: Bitmap): Bitmap {
        val currentWidth = bitmap.width
        val currentHeight = bitmap.height
        return if (currentWidth > currentHeight) {
            rotateBitmap(bitmap, 90f)
        } else {
            bitmap
        }
    }
    fun convertToLandscape(bitmap: Bitmap): Bitmap {
        val currentWidth = bitmap.width
        val currentHeight = bitmap.height
        return if (currentWidth < currentHeight) {
            rotateBitmap(bitmap, 90f)
        } else {
            bitmap
        }
    }
    fun rotateBitmap(bitmap: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}