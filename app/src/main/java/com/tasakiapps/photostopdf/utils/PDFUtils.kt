package com.tasakiapps.photostopdf.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.tasakiapps.photostopdf.model.PdfModel

object PDFUtils {

    fun getExternalPDFFileList(context: Context): ArrayList<PdfModel>? {
        val cr = context.contentResolver
        val uri = MediaStore.Files.getContentUri("external")
        val projection =
            arrayOf(MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DISPLAY_NAME)
        val selection =
            (MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE)
        val selectionArgs: Array<String>? = null
        val selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
        val selectionArgsPdf = arrayOf(mimeType)
        val cursor = cr.query(uri, projection, selectionMimeType, selectionArgsPdf, null)!!
        val uriList: ArrayList<PdfModel> = ArrayList<PdfModel>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val columnIndex = cursor.getColumnIndex(projection[0])
            val fileId = cursor.getLong(columnIndex)
            val fileUri = Uri.parse("$uri/$fileId")
            val displayName = cursor.getString(cursor.getColumnIndex(projection[1]))
            uriList.add(PdfModel(displayName, getPdfPathFromUri(context, fileUri)!!))
            cursor.moveToNext()
        }
        cursor.close()
        return uriList
    }
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
}