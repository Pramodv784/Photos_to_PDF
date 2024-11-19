package com.tasakiapps.photostopdf.extension

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.tasakiapps.photostopdf.model.GridViewItem
import com.tasakiapps.photostopdf.utils.Utils
import java.io.File
import java.io.FileFilter
import java.util.*


@WorkerThread
fun RetrivePhoto(context: Context,directoryPath: String):List<GridViewItem> {
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

            val uri = Utils.getUriFromFile(context,file1)


                list.add(GridViewItem(file1.name, uri.toString(), file1.length()))
        }
    }
    Log.d("Photo list>>> ","${list.size}")
    return list

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
             //  val filePath = Utils.getPdfPathFromUri(this,uri)
          //  val file = File(filePath)
          // Log.e("TAG", "FilePath1URi: "+filePath)
            results.add(GridViewItem("",uri.toString(),0))


//            }
        }
    }

    cursor?.close()
    return results
}

