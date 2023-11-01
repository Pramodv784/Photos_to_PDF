package com.tasakiapps.photostopdf.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.itextpdf.text.pdf.PdfReader
import java.io.File
import java.io.IOException

object GetThumbnail {
    fun generateThumbnailFromPdf(context: Context, pdfFile: File): Bitmap? {
        try {
            val parcelFileDescriptor: ParcelFileDescriptor =
                ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)

            val pdfRenderer = PdfRenderer(parcelFileDescriptor)
            val pageCount = pdfRenderer.pageCount

            // For simplicity, we're just grabbing the first page. You can modify this as needed.
            val pageIndex = 0
            val page = pdfRenderer.openPage(pageIndex)

            // Create a Bitmap object and render the page content onto it
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            // Close the page and the PdfRenderer after use
            page.close()
            pdfRenderer.close()
            parcelFileDescriptor.close()

            return bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
    fun isPdfPasswordProtected(filePath: String): Boolean {
        try {
            val pdfReader = PdfReader(filePath)
            return pdfReader.isEncrypted
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}