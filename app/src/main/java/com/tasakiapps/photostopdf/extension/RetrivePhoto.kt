package com.tasakiapps.photostopdf.extension

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.annotation.WorkerThread
import com.tasakiapps.photostopdf.model.GridViewItem
import java.io.File
import java.io.FileFilter
import java.util.*


@WorkerThread
fun Context.RetrivePhoto(directoryPath: String):List<GridViewItem> {
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

