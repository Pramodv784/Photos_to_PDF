package com.tasakiapps.photostopdf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    }
}