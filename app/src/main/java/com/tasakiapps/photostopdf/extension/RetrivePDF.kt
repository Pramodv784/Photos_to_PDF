package com.tasakiapps.photostopdf.extension

import android.content.Context
import androidx.annotation.WorkerThread
import com.tasakiapps.photostopdf.model.PdfModel
import java.io.File
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
