package com.tasakiapps.photostopdf.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.tasakiapps.photostopdf.BuildConfig
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.databinding.ActivityPdfviewBinding
import java.io.File


class PDFViewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPdfviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfview)
        binding = ActivityPdfviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "File Name"
            titleColor = getColor(R.color.black)
            this.setDisplayHomeAsUpEnabled(true)
            this.setHomeAsUpIndicator(R.drawable.back_arrow)

        }
        initViews()
    }

    private fun initViews() {
        var pdf_uri = intent.getStringExtra("pdf_path")

        Log.d("PDF URL>>>>>","$pdf_uri")
        binding.pdfViewer.fromFile(pdf_uri!!).show()

        binding.ivShare.setOnClickListener {

            val uri: Uri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                File(pdf_uri)
            )
            val intent = Intent()
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.action = Intent.ACTION_SEND
            intent.type = "application/pdf"

            intent.putExtra(Intent.EXTRA_SUBJECT, "")
            intent.putExtra(Intent.EXTRA_TEXT, "")
            intent.putExtra(Intent.EXTRA_STREAM, uri)

            try {
             startActivity(Intent.createChooser(intent, "Share Via"))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "No Sharing App Found", Toast.LENGTH_SHORT).show()
            }
        }

    }
}