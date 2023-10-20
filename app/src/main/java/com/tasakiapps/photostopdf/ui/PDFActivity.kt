package com.tasakiapps.photostopdf.ui

import android.net.Uri
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import com.tasakiapps.photostopdf.adaptor.PDFAdapter
import com.tasakiapps.photostopdf.databinding.ActivityPdfactivityBinding
import com.tasakiapps.photostopdf.model.PdfModel


class PDFActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPdfactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        val list  = getExternalPDFFileList()
        list?.forEach {
            Log.d("pdf>>>>","${it.fileName}")
        }
        binding.rvPdf.adapter = PDFAdapter(list?.reversed()!!,this@PDFActivity)

    }
    private fun getExternalPDFFileList(): ArrayList<PdfModel>? {
        val cr = contentResolver
        val uri = MediaStore.Files.getContentUri("external")
        val projection =
            arrayOf(MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DISPLAY_NAME)
        val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE)
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
            uriList.add(PdfModel(displayName, fileUri))
            cursor.moveToNext()
        }
        cursor.close()
        return uriList
    }
}