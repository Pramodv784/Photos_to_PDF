package com.tasakiapps.photostopdf.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.tasakiapps.photostopdf.model.GridViewItem
import java.io.File
import java.io.FileFilter
import java.util.*


@WorkerThread
fun RetrivePhoto(directoryPath: String):List<GridViewItem> {
  //  val filePath = "/storage/emulated/0/Pictures"

    val file = File(directoryPath)
  //  val files = file.listFiles()
    val jpgfiles: Array<File> =
        file.listFiles(FileFilter { file -> file.path.endsWith(".jpg") ||
                file.path.endsWith(".jpeg") || file.path.endsWith(".png")})!!
    var list = ArrayList<GridViewItem>()
    if (jpgfiles != null) {
        val sortedFiles = jpgfiles.sortedWith(Comparator { file1, file2 ->
            val lastModified1 = file1.lastModified()
            val lastModified2 = file2.lastModified()

            // Sort in descending order (latest modified first)
            lastModified2.compareTo(lastModified1)
        })

        for (file1 in sortedFiles) {

                list.add(GridViewItem(file1.name, file1.path, file1.length()))
        }
    }
    Log.d("Photo list>>> ","${list.size}")
    return list

}

@WorkerThread
fun RetriveAllImages(context: Context): List<GridViewItem> {
    val imageList = mutableListOf<GridViewItem>()
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.SIZE
    )

    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        null
    )

    cursor?.use {
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val title = cursor.getString(titleColumn)
            val path = cursor.getString(pathColumn)
            val size = cursor.getLong(sizeColumn)

            // Load the bitmap (consider adding size limits for large files)
            val bitmap: Bitmap? = BitmapFactory.decodeFile(path)

            // Create a GridViewItem and add it to the list
            val item = GridViewItem(
                title = title,
                path = path,
                size = size,
                bitmap = bitmap
            )
            imageList.add(item)
        }
    }

    return imageList
}

