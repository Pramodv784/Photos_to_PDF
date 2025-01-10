package com.tasakiapps.photostopdf.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.media.ExifInterface
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.lowagie.text.Document
import com.lowagie.text.Image
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfWriter
import com.tasakiapps.photostopdf.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.min

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
//    private fun savePdfToFile( pdfDocument: PdfDocument, pdfFileName: String) {
//        try {
//            var pdfDoc = pdfDocument
//            // Get the directory for storing the PDF file
//            val directory = File(Environment.getExternalStorageDirectory(), "PDFFiles")
//            if (!directory.exists()) {
//                directory.mkdirs()
//            }
//
//            // Create the PDF file
//            val pdfFile = File(directory, pdfFileName)
//
//            // Create a FileOutputStream for the PDF file
//            val fileOutputStream = FileOutputStream(pdfFile)
//
//            // Write the contents of the PdfDocument to the FileOutputStream
//            pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(PdfWriter(fileOutputStream))
//
//            // Close the FileOutputStream
//            fileOutputStream.close()
//            pdfCallback.invoke(true,pdfFileName)
//            // Inform the user that the PDF has been saved
//            // You may want to show a Toast or update the UI accordingly
//
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }

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




//    fun createPdfWithMultipleImages(
//        imagePaths: List<String>,
//        dest: String,
//        quality: Int,
//        isOrientation: String
//    ) {
//        pdfCallback.invoke(false, "")
//
//        val directory = File(Environment.getExternalStorageDirectory(), "PDFFiles")
//        if (!directory.exists()) {
//            directory.mkdirs()
//        }
//
//        // Create the PDF file
//        val pdfFile = File(directory, dest)
//
//        // Create a FileOutputStream for the PDF file
//        val fileOutputStream = FileOutputStream(pdfFile)
//
//        val pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(PdfWriter(fileOutputStream))
//        val doc = Document(
//            pdfDoc,
//
//        )
//
//        for (imagePath in imagePaths) {
//            val compressedImageBytes = compressImage(imagePath, quality)
//
//            // Create an Image instance from the image path
//            val img = Image(ImageDataFactory.create(compressedImageBytes))
//
//            // Calculate aspect ratio
//            val aspectRatio = img.imageWidth.toFloat() / img.imageHeight.toFloat()
//            img.setPadding(5.toFloat())
//
//
//            if(isOrientation.equals("Vertical")){
//                doc.pdfDocument.defaultPageSize =PageSize.A4
//            }
//            else if(isOrientation.equals("Horizontal")){
//                doc.pdfDocument.defaultPageSize =PageSize.A4.rotate()
//            }
////            else{
////                if (aspectRatio > 1) { // Landscape image
////                    img.setWidth(UnitValue.createPointValue(img.imageWidth.toFloat()))
////                    img.setHeight(UnitValue.createPointValue(img.imageHeight.toFloat()))
////                } else { // Portrait image
////                    img.setWidth(UnitValue.createPointValue(img.imageHeight.toFloat())) // Swap width and height for portrait
////                    img.setHeight(UnitValue.createPointValue(img.imageWidth.toFloat()))
////                }
////            }
//
//            // Add a new page to the document
//
//
//            // Add image to the document
//            doc.add(img)
//        }
//
//
//        pdfCallback.invoke(true,dest)
//
//        doc.close()
//    }
//
//    fun createPdfWithMultipleImages2(
//        imagePaths: List<String>,
//        dest: String,
//        quality: Int,
//        isOrientation: String,
//        password:String
//    ) {
//        try {
//            val directory = File(Environment.getExternalStorageDirectory(), "PDFFiles")
//            if (!directory.exists()) {
//                directory.mkdirs()
//            }
//
//            // Create the PDF file
//            val pdfFile = File(directory, dest)
//
////            val writer = com.itextpdf.kernel.pdf.PdfWriter(pdfFile).apply {
////                setStandardEncryption(
////                    userPassword.toByteArray(),
////                    ownerPassword.toByteArray(),
////                    com.itextpdf.kernel.pdf.PdfWriter.ALLOW_PRINTING,
////                    com.itextpdf.kernel.pdf.encryption.StandardEncryption128
////                )
////            }
//
//            // Create a FileOutputStream for the PDF file
//            val pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(PdfWriter(pdfFile))
//            val doc = Document(pdfDoc)
//
//            // Set page size based on orientation
//            val pageSize = if (isOrientation.equals("Vertical", true)) PageSize.A4 else PageSize.A4.rotate()
//            pdfDoc.defaultPageSize = pageSize
//
//            // Iterate through images and add them to the PDF
//            for ((index, imagePath) in imagePaths.withIndex()) {
//                val compressedImageBytes = compressImage(imagePath, quality)
//                val img = Image(ImageDataFactory.create(compressedImageBytes))
//
//                // Set margins around the image
//                img.setMargins(10f, 10f, 10f, 10f)
//
//                // Scale the image to fit within the page while maintaining the aspect ratio
//                val availableWidth = pageSize.width - doc.leftMargin - doc.rightMargin
//                val availableHeight = pageSize.height - doc.topMargin - doc.bottomMargin
//
//                img.scaleToFit(availableWidth, availableHeight)
//
////                // Center the image on the page
////                val xOffset = (availableWidth - img.imageScaledWidth) / 2
////                val yOffset = (availableHeight - img.imageScaledHeight) / 2
////
////                img.setFixedPosition(doc.leftMargin + xOffset, doc.bottomMargin + yOffset)
//
//                // Add the image to the document
//                doc.add(img)
//
//                // Add a page number at the bottom
//                val pageNumber = Paragraph("Page ${index + 1}")
//                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
//                    .setFontSize(10f)
//                doc.add(pageNumber)
//
//                // Add a new page if not the last image
//                if (index != imagePaths.lastIndex) {
//                    doc.add(AreaBreak())
//                }
//            }
//
//            doc.close()
//            pdfCallback.invoke(true, dest)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            pdfCallback.invoke(false, e.message ?: "Error creating PDF")
//        }
//    }



    fun createPdfWithMultipleImages34(
        imagePaths: List<String>,
        dest: String,
        quality: Int,
        isOrientation: String
    ) {
        try {
            val directory = File(Environment.getExternalStorageDirectory(), "PDFFiles")
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // Create the PDF file
            val pdfFile = File(directory, dest)

            // Initialize the Document
            val document = Document()

            // Create PdfWriter
            val writer = PdfWriter.getInstance(document, FileOutputStream(pdfFile))

            // Set the orientation for the document
            val pageSize = if (isOrientation.equals("Vertical", true)) PageSize.A4 else PageSize.A4.rotate()
            document.pageSize = pageSize

            // Open the document to start adding content
            document.open()

            // Iterate through the image paths and add them to the document
            for ((index, imagePath) in imagePaths.withIndex()) {
                val imgFile = File(imagePath)
                val imgBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

                val byteArrayOutputStream = ByteArrayOutputStream()
                imgBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()
                val image = Image.getInstance(imageBytes) // Convert Bitmap to byte array

                // Scale image to fit the page, maintaining the aspect ratio
                val availableWidth = pageSize.width - document.leftMargin() - document.rightMargin()
                val availableHeight = pageSize.height - document.topMargin() - document.bottomMargin()
                image.scaleToFit(availableWidth, availableHeight)

                // Center image on page
//                val xOffset = (availableWidth - image.scaledWidth) / 2
//                val yOffset = (availableHeight - image.scaledHeight) / 2
//                image.setAbsolutePosition(document.leftMargin() + xOffset, document.bottomMargin() + yOffset)

                // Add image to the document
                document.add(image)

                // Add page number for each image
                val pageNumber = Paragraph("Page ${index + 1}")
                pageNumber.alignment = Paragraph.ALIGN_CENTER
               // pageNumber.font = Font(Font.FontFamily.TIMES_ROMAN, 10f)
                document.add(pageNumber)

                // Add a new page if it's not the last image
                if (index != imagePaths.lastIndex) {
                    document.newPage()
                }
            }

            // Close the document
            document.close()

            // Notify success
            pdfCallback.invoke(true, dest)

        } catch (e: Exception) {
            e.printStackTrace()
            // Notify failure
            pdfCallback.invoke(false, e.message ?: "Error creating PDF")
        }
    }

    fun createPdfWithMultipleImages(
        imagePaths: List<String>,
        dest: String,
        quality: Int, // Add quality parameter (1-100, where 100 is best quality)
        isOrientation: String,
        context: Context
    ) {
        try {
            val directory = File(Environment.getExternalStorageDirectory(), "PDFFiles")
            if (!directory.exists()) {
                directory.mkdirs()
            }

            val pdfFile = File(directory, dest)
            val pdfDocument = PdfDocument()

            // Set page dimensions based on orientation
            val dpi = 150 // Desired DPI
            val inchToPointFactor = dpi / 72.0
            val isVertical = isOrientation.equals("Vertical", true)
            val pageWidth = if (isVertical) (595 * inchToPointFactor).toInt() else (842 * inchToPointFactor).toInt()
            val pageHeight = if (isVertical) (842 * inchToPointFactor).toInt() else (595 * inchToPointFactor).toInt()

            val margin = 50 // Margin in pixels (adjust as needed)

            // Define colors
            val greyColor = ContextCompat.getColor(context, R.color.color_grey)
            val whiteColor = ContextCompat.getColor(context, android.R.color.white)

            for ((index, imagePath) in imagePaths.withIndex()) {
                val imgFile = File(imagePath)
                var imgBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

                // Handle image rotation using EXIF metadata
                val exif = ExifInterface(imgFile.absolutePath)
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                imgBitmap = when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(imgBitmap, 90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(imgBitmap, 180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(imgBitmap, 270f)
                    else -> imgBitmap
                }

                // Compress the image with the given quality parameter
                val outputStream = ByteArrayOutputStream()
                imgBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                val compressedBitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())

                // Scale the compressed bitmap to fit inside the page with margins
                val imageAspectRatio = compressedBitmap.width.toFloat() / compressedBitmap.height.toFloat()
                val contentWidth = pageWidth - 2 * margin
                val contentHeight = pageHeight - 2 * margin
                val contentAspectRatio = contentWidth.toFloat() / contentHeight.toFloat()

                val scaledWidth: Int
                val scaledHeight: Int
                if (imageAspectRatio > contentAspectRatio) {
                    // Image is wider
                    scaledWidth = contentWidth
                    scaledHeight = (contentWidth / imageAspectRatio).toInt()
                } else {
                    // Image is taller
                    scaledHeight = contentHeight
                    scaledWidth = (contentHeight * imageAspectRatio).toInt()
                }

                val scaledBitmap = Bitmap.createScaledBitmap(
                    compressedBitmap,
                    scaledWidth,
                    scaledHeight,
                    true
                )

                // Create a new page
                val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, index + 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas

                // Draw grey background (entire page background)
                val backgroundPaint = Paint()
                backgroundPaint.color = greyColor
                canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), backgroundPaint)

                // Draw white container (content area background)
                val containerLeft = margin.toFloat()
                val containerTop = margin.toFloat()
                val containerRight = (pageWidth - margin).toFloat()
                val containerBottom = (pageHeight - margin).toFloat()

                val containerPaint = Paint()
                containerPaint.color = whiteColor
                canvas.drawRect(containerLeft, containerTop, containerRight, containerBottom, containerPaint)

                // Draw the scaled image centered within the content area
                val xPos = containerLeft + (contentWidth - scaledBitmap.width) / 2f
                val yPos = containerTop + (contentHeight - scaledBitmap.height) / 2f
                canvas.drawBitmap(scaledBitmap, xPos, yPos, null)

                // Recycle the scaled and compressed bitmap
                scaledBitmap.recycle()
                compressedBitmap.recycle()

                // Finish the page
                pdfDocument.finishPage(page)
            }

            // Write the document to file
            pdfDocument.writeTo(FileOutputStream(pdfFile))

            // Close the document
            pdfDocument.close()

            pdfCallback.invoke(true, dest)

            // Notify success
            println("PDF created at: ${pdfFile.absolutePath}")

        } catch (e: Exception) {
            e.printStackTrace()
            println("Error creating PDF: ${e.message}")
        }
    }




    fun createPdfWithMultipleImages2(
        imagePaths: List<String>,
        dest: String,
        quality: Int, // Compression quality (1-100, where 100 is best quality)
        isOrientation: String,
        context: Context
    ) {
        try {
            val directory = File(Environment.getExternalStorageDirectory(), "PDFFiles")
            if (!directory.exists()) {
                directory.mkdirs()
            }

            val pdfFile = File(directory, dest)
            val pdfDocument = PdfDocument()

            // Define page dimensions based on orientation
            val isVertical = isOrientation.equals("Vertical", true)
            val dpi = 150 // Lower DPI for smaller file size
            val inchToPointFactor = dpi / 72.0
            val pageWidth = if (isVertical) (595 * inchToPointFactor).toInt() else (842 * inchToPointFactor).toInt()
            val pageHeight = if (isVertical) (842 * inchToPointFactor).toInt() else (595 * inchToPointFactor).toInt()

            val margin = 50 // Margins in points (adjust as needed)

            for ((index, imagePath) in imagePaths.withIndex()) {
                val imgFile = File(imagePath)

                // Decode image with resizing to reduce memory and improve performance
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true // Load bounds only
                    BitmapFactory.decodeFile(imgFile.absolutePath, this)
                    val targetWidth = pageWidth - 2 * margin
                    val targetHeight = pageHeight - 2 * margin
                    val scaleFactor = maxOf(
                        outWidth / targetWidth,
                        outHeight / targetHeight,
                        1 // Ensure at least 1 (no upscale)
                    )
                    inSampleSize = scaleFactor
                    inJustDecodeBounds = false // Load actual image with scaling
                }

                val originalBitmap = BitmapFactory.decodeFile(imgFile.absolutePath, options) ?: continue

                // Handle image rotation using EXIF metadata
                val exif = ExifInterface(imgFile.absolutePath)
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                val rotatedBitmap = when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(originalBitmap, 90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(originalBitmap, 180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(originalBitmap, 270f)
                    else -> originalBitmap
                }

                // Compress the image with the given quality parameter
                val outputStream = ByteArrayOutputStream()
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                val compressedBitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())

                // Create a new page
                val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, index + 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas

                // Draw grey background (entire page background)
                val backgroundPaint = Paint()
                backgroundPaint.color = ContextCompat.getColor(context, R.color.color_grey)
                canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), backgroundPaint)

                // Draw image centered within content area
                val contentWidth = pageWidth - 2 * margin
                val contentHeight = pageHeight - 2 * margin
                val xPos = margin.toFloat() + (contentWidth - compressedBitmap.width) / 2f
                val yPos = margin.toFloat() + (contentHeight - compressedBitmap.height) / 2f
                canvas.drawBitmap(compressedBitmap, xPos, yPos, null)

                // Recycle bitmaps to free memory
                if (rotatedBitmap !== originalBitmap) rotatedBitmap.recycle()
                originalBitmap.recycle()
                compressedBitmap.recycle()

                // Finish the page
                pdfDocument.finishPage(page)
            }

            // Write the document to file
            pdfDocument.writeTo(FileOutputStream(pdfFile))
            pdfDocument.close()
            pdfCallback.invoke(true, dest)

            println("PDF created at: ${pdfFile.absolutePath}")

        } catch (e: Exception) {
            e.printStackTrace()
            println("Error creating PDF: ${e.message}")
        }
    }





    // Function to rotate a bitmap
    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }


}