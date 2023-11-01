package com.tasakiapps.photostopdf.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageToPDF(private val context: Context) {

    lateinit var pdfCallback:(Boolean) -> Unit
    fun convertImagesToPdf(imagePaths: List<String>, pdfFilePath: String): Boolean {
        val document = Document()
        try {
            PdfWriter.getInstance(document, FileOutputStream(pdfFilePath))
            document.open()

            for (imagePath in imagePaths) {
                val image = Image.getInstance(imagePath)
                document.add(image)
            }

            document.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Converted Failed ",">>>>>>>$e")
            return false
        }
    }

    suspend fun convertImagesToPdf(context: Context, imagePaths: List<String>, pdfFileName: String) {
        // Create a new PdfDocument
        val pdfDocument = PdfDocument()
        pdfCallback.invoke(false)

        // Iterate through the list of image paths
        for (imagePath in imagePaths) {
            // Decode the image file to a Bitmap
            val bitmap = BitmapFactory.decodeFile(imagePath)

            // Create a new page in the PDF document
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            // Draw the bitmap on the PDF page
            val canvas: Canvas = page.canvas
            canvas.drawColor(Color.WHITE)
            canvas.drawBitmap(bitmap, 0f, 0f, null)

            // Finish the page
            pdfDocument.finishPage(page)

            // Recycle the bitmap to free up memory
            bitmap.recycle()
        }

        // Save the PDF to a file
        savePdfToFile(context, pdfDocument, pdfFileName)

        // Close the PdfDocument
        pdfDocument.close()
    }
    private fun savePdfToFile(context: Context, pdfDocument: PdfDocument, pdfFileName: String) {
        try {
            // Get the directory for storing the PDF file
            val directory = File(Environment.getExternalStorageDirectory(), "PDFFiles")
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // Create the PDF file
            val pdfFile = File(directory, pdfFileName)

            // Create a FileOutputStream for the PDF file
            val fileOutputStream = FileOutputStream(pdfFile)

            // Write the contents of the PdfDocument to the FileOutputStream
            pdfDocument.writeTo(fileOutputStream)

            // Close the FileOutputStream
            fileOutputStream.close()
            pdfCallback.invoke(true)
            // Inform the user that the PDF has been saved
            // You may want to show a Toast or update the UI accordingly

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}