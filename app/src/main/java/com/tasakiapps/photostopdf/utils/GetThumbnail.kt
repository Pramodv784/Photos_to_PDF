package com.tasakiapps.photostopdf.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.media.ThumbnailUtils
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import com.itextpdf.text.pdf.PdfReader
import java.io.File
import java.io.IOException

object GetThumbnail {
    fun generateThumbnailFromPdf(context: Context, pdfFile: File): Bitmap? {

        val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(fileDescriptor)

        // Generate a thumbnail for the first page
        val page = pdfRenderer.openPage(0)
        val thumbnailBitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(thumbnailBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()

        // Save the thumbnail to MediaStore or use it as needed
      //  val thumbnailUri = saveThumbnailToMediaStore(thumbnailBitmap, "PDF_Thumbnail", "thumbnail.png")
        // Use thumbnailUri as needed (e.g., set it to an ImageView)

        // Close resources
        pdfRenderer.close()
        fileDescriptor.close()
        return thumbnailBitmap
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