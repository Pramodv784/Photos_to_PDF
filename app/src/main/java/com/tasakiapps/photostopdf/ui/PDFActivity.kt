package com.tasakiapps.photostopdf.ui

import android.content.ContentResolver
import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.adaptor.PDFAdapter
import com.tasakiapps.photostopdf.databinding.ActivityPdfactivityBinding
import com.tasakiapps.photostopdf.extension.changeStatusBarColor
import com.tasakiapps.photostopdf.model.PdfModel


class PDFActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        this.changeStatusBarColor(R.color.color_background)
        val list = getExternalPDFFileList()
        list?.forEach {
            Log.d("pdf>>>>", "${it.fileName}")
        }
        binding.rvPdf.adapter = PDFAdapter(list?.reversed()!!, this@PDFActivity)
        binding.tvPdfConverted.setOnClickListener {
            changeBackground(true, binding.tvPdfConverted, binding.tvPdfReader)
        }
        binding.tvPdfReader.setOnClickListener {
            changeBackground(false, binding.tvPdfConverted, binding.tvPdfReader)

        }
    }

    private fun getExternalPDFFileList(): ArrayList<PdfModel>? {
        val cr = contentResolver
        val uri = MediaStore.Files.getContentUri("external")
        val projection =
            arrayOf(MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DISPLAY_NAME)
        val selection =
            (MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE)
        val selectionArgs: Array<String>? = null
        val selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
        val selectionArgsPdf = arrayOf(mimeType)
        val cursor = cr.query(uri, projection, selectionMimeType, selectionArgsPdf, null)!!
        val uriList: ArrayList<PdfModel> = ArrayList<PdfModel>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val columnIndex = cursor.getColumnIndex(projection[0])
            val fileId = cursor.getLong(columnIndex)
            val fileUri = Uri.parse("$uri/$fileId")
            val displayName = cursor.getString(cursor.getColumnIndex(projection[1]))
            uriList.add(PdfModel(displayName, getPdfPathFromUri(this@PDFActivity, fileUri)!!))
            cursor.moveToNext()
        }
        cursor.close()
        return uriList
    }



    fun getPdfPathFromUri(context: Context, uri: Uri): String? {
        var filePath: String? = null

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val contentResolver: ContentResolver = context.contentResolver

        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = cursor.getString(columnIndex)
            }
        }

        return filePath
    }

    fun changeBackground(
        isBackGround: Boolean, firstView: AppCompatTextView, secondView: AppCompatTextView
    ) {
        if (isBackGround) {
            firstView.apply {
                this.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@PDFActivity, R.color.color_text_red
                    )
                );
                setTextColor(ContextCompat.getColor(this@PDFActivity, R.color.white))

                secondView.apply {
                    backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@PDFActivity, R.color.color_grey
                        )
                    )
                    setTextColor(ContextCompat.getColor(this@PDFActivity, R.color.color_text_grey))

                }


            }
        } else {
            secondView.apply {
                this.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@PDFActivity, R.color.color_text_red
                    )
                );
                setTextColor(ContextCompat.getColor(this@PDFActivity, R.color.white))

                firstView.apply {
                    backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@PDFActivity, R.color.color_grey
                        )
                    )
                    setTextColor(ContextCompat.getColor(this@PDFActivity, R.color.color_text_grey))

                }


            }
        }
    }
}