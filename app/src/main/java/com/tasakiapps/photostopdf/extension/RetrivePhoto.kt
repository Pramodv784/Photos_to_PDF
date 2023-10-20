package com.tasakiapps.photostopdf.extension

import android.content.Context
import android.media.Image
import android.util.Log
import androidx.annotation.WorkerThread
import com.tasakiapps.photostopdf.model.GridViewItem
import java.io.File


@WorkerThread
fun Context.RetrivePhoto(directoryPath: String):List<GridViewItem> {
  //  val filePath = "/storage/emulated/0/Pictures"
    val file = File(directoryPath)
    val files = file.listFiles()
    var list = ArrayList<GridViewItem>()
    if (files != null) {
        for (file1 in files) {
            if (file1.path.endsWith(".png") || file1.path.endsWith(".jpg")) {
                list.add(GridViewItem(file1.name, file1.path, file1.length()))
            }
        }
    }
    Log.d("Photo list>>> ","${list.size}")
    return list

}

