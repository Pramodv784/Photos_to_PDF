package com.tasakiapps.photostopdf.extension

import android.app.Activity
import android.view.WindowManager
import androidx.core.content.ContextCompat

/*
@WorkerThread
fun Context.RetrivePDF(dir: File):ArrayList<PdfModel>{

    val pdfPattern = ".pdf"
    var pdflist = ArrayList<PdfModel>()

    val FileList = dir.listFiles()

    if (FileList != null) {
        for (i in FileList.indices) {
            if (FileList[i].isDirectory) {
                this.RetrivePDF(FileList[i])
            } else {
                if (FileList[i].name.endsWith(pdfPattern)) {
                    //here you have that file.
                    pdflist.add(PdfModel(FileList[i].name,FileList[i].totalSpace.toString(),FileList[i].path))
                }
            }
        }
    }
    return pdflist
}*/


fun Activity.changeStatusBarColor(color: Int) {
    val window = this.window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.statusBarColor = ContextCompat.getColor(this, color)

}

