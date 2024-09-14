package com.tasakiapps.photostopdf.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.text.style.BackgroundColorSpan
import android.widget.Toast
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.properties.Background
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.UnitValue
import com.tasakiapps.photostopdf.R


import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
class ImageToPDF(private val context: Context) {

    lateinit var pdfCallback:(Boolean, String) -> Unit


    suspend fun convertImagesToPdf(context: Context, imagePaths: List<String>, pdfFileName: String,quality:Int) {
        // Create a new PdfDocument
        val pdfDocument = PdfDocument()
        pdfCallback.invoke(false,"")

        // Iterate through the list of image paths
        for (imagePath in imagePaths) {
            // Decode the image file to a Bitmap
           // var compressFile = compressImage(File(imagePath),context,50)
            val bitmap = BitmapFactory.decodeFile(imagePath)
           // val finalBitmap = convertLandscapeToPortrait(bitmap)

            // Create a new page in the PDF document
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            // Draw the bitmap on the PDF page
            val canvas: Canvas = page.canvas
            canvas.drawColor(Color.WHITE)
            canvas.drawBitmap(bitmap, 10f, 10f, null)

            // Finish the page
            pdfDocument.finishPage(page)

            // Recycle the bitmap to free up memory
            bitmap.recycle()
        }

        // Save the PDF to a file
      //  savePdfToFile( pdfDocument, pdfFileName)

        // Close the PdfDocument
        pdfDocument.close()
    }




    fun compressImage(inputFile: File, context: Context,quality:Int): File {
        val originalBitmap = BitmapFactory.decodeFile(inputFile.absolutePath)
        val outputStream = ByteArrayOutputStream()

        // You can adjust the compression quality as needed
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        val compressedByteArray = outputStream.toByteArray()

        // Create a new file to store the compressed image
        val compressedFile = File(context.cacheDir, "compressed_image.jpg")

        try {
            val fileOutputStream = FileOutputStream(compressedFile)
            fileOutputStream.write(compressedByteArray)
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return compressedFile
    }



    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
    private fun savePdfToFile( pdfDocument: com.itextpdf.kernel.pdf.PdfDocument, pdfFileName: String) {
        try {
            var pdfDoc = pdfDocument
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
            pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(PdfWriter(fileOutputStream))

            // Close the FileOutputStream
            fileOutputStream.close()
            pdfCallback.invoke(true,pdfFileName)
            // Inform the user that the PDF has been saved
            // You may want to show a Toast or update the UI accordingly

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun convertLandscapeToPortrait(originalBitmap: Bitmap): Bitmap {
        val width = originalBitmap.width
        val height = originalBitmap.height

        if (width < height) {
            // Already in portrait orientation, no need to convert
            return originalBitmap
        }

        val portraitBitmap = Bitmap.createBitmap(height, width, originalBitmap.config)

        val canvas = android.graphics.Canvas(portraitBitmap)
        val matrix = android.graphics.Matrix()
        matrix.setRectToRect(
            android.graphics.RectF(0f, 0f, width.toFloat(), height.toFloat()),
            android.graphics.RectF(0f, 0f, height.toFloat(), width.toFloat()),
            android.graphics.Matrix.ScaleToFit.FILL
        )

        canvas.drawBitmap(originalBitmap, matrix, null)

        return portraitBitmap
    }

    private suspend fun createPdf(context: Context, data: ArrayList<File>,fileName:String) {
      //  val pdfFile = File(context.externalCacheDir?.absolutePath + File.separator + "TemporaryPDF_${System.currentTimeMillis()}.pdf")
        Toast.makeText(context, "Creating PDF, Please wait..", Toast.LENGTH_SHORT).show()
        val document = PdfDocument()
        try {

            for (item in data) {
                val bitmap = BitmapFactory.decodeFile(item.absolutePath)
                val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
                val page = document.startPage(pageInfo)
                val canvas = page.canvas
                val paint = Paint()
                paint.color = Color.parseColor("#ffffff")
                canvas.drawPaint(paint)
                canvas.drawBitmap(bitmap, 0f, 0f, null)
                document.finishPage(page)
            }
           // savePdfToFile( document, fileName)

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            document.close()
        }




    }




    fun createPdfWithMultipleImages(
        imagePaths: List<String>,
        dest: String,
        quality: Int,
        isOrientation: String
    ) {
        pdfCallback.invoke(false, "")

        val directory = File(Environment.getExternalStorageDirectory(), "PDFFiles")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Create the PDF file
        val pdfFile = File(directory, dest)

        // Create a FileOutputStream for the PDF file
        val fileOutputStream = FileOutputStream(pdfFile)

        val pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(PdfWriter(fileOutputStream))
        val doc = Document(
            pdfDoc,

        )

        for (imagePath in imagePaths) {
            val compressedImageBytes = compressImage(imagePath, quality)

            // Create an Image instance from the image path
            val img = Image(ImageDataFactory.create(compressedImageBytes))

            // Calculate aspect ratio
            val aspectRatio = img.imageWidth.toFloat() / img.imageHeight.toFloat()
            img.setPadding(5.toFloat())


            if(isOrientation.equals("Vertical")){
                doc.pdfDocument.defaultPageSize =PageSize.A4
            }
            else if(isOrientation.equals("Horizontal")){
                doc.pdfDocument.defaultPageSize =PageSize.A4.rotate()
            }
//            else{
//                if (aspectRatio > 1) { // Landscape image
//                    img.setWidth(UnitValue.createPointValue(img.imageWidth.toFloat()))
//                    img.setHeight(UnitValue.createPointValue(img.imageHeight.toFloat()))
//                } else { // Portrait image
//                    img.setWidth(UnitValue.createPointValue(img.imageHeight.toFloat())) // Swap width and height for portrait
//                    img.setHeight(UnitValue.createPointValue(img.imageWidth.toFloat()))
//                }
//            }

            // Add a new page to the document


            // Add image to the document
            doc.add(img)
        }


        pdfCallback.invoke(true,dest)

        doc.close()
    }


    fun compressImage(imagePath: String, quality: Int): ByteArray {
        val bitmap = BitmapFactory.decodeFile(imagePath)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }

}