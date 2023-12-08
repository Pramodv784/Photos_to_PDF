package com.tasakiapps.photostopdf

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.tasakiapps.photostopdf.adaptor.UserSelectImageAdapter
import com.tasakiapps.photostopdf.databinding.ActivityImagesBinding
import com.tasakiapps.photostopdf.extension.RetrivePhoto
import com.tasakiapps.photostopdf.extension.changeStatusBarColor
import com.tasakiapps.photostopdf.model.GridViewItem
import com.tasakiapps.photostopdf.ui.PDFViewActivity
import com.tasakiapps.photostopdf.utils.ImageToPDF
import com.tasakiapps.photostopdf.utils.Utils
import com.tasakiapps.photostopdf.utils.Utils.getFileUri
import com.tasakiapps.photostopdf.utils.Utils.retrievePhotos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class ImagesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityImagesBinding
    val directories = ArrayList<String>()
    private var selectedImages = ArrayList<GridViewItem>()
    private lateinit var imageAdaptor: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        this.changeStatusBarColor(R.color.color_background)
        binding.spinner.onItemSelectedListener = this
        imageAdaptor = ImageAdapter( this)

        var listFolder = ArrayList<String>()
        getImageDirectories(this)?.forEach {
            var folderName = it.split("/").last()
            listFolder.add(folderName)
        }
        val adapter = ArrayAdapter(
            this, android.R.layout.simple_dropdown_item_1line, listFolder
        )
        binding.spinner.adapter = adapter






        binding.pdfBT.setOnClickListener {
            binding.progress.visibility = View.VISIBLE
            if (selectedImages.isNotEmpty()) {
                val imagePaths = ArrayList<String>()
                val converter = ImageToPDF(this)

                selectedImages.forEach {
                    imagePaths.add(it.path)
                }/*  if (converter.convertImagesToPdf(imagePaths, pdfFilePath)) {

                      // Conversion successful
                      Log.d("Conversion successful",">>>>>>>")
                      Log.d("Conversion Path",">>>>>>>${pdfFilePath}")
                  } else {
                      // Conversion failed
                      Log.d("Conversion Failed",">>>>>>>")
                  }*/
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("PDF File Name Time>>>","${System.currentTimeMillis()}")
                    converter.convertImagesToPdf(this@ImagesActivity,imagePaths,
                        "File${System.currentTimeMillis()}.pdf")
                }
                converter.pdfCallback={ status, fileName ->
                    if(status){
                        Log.d("PDF File Name >>>","${fileName}")
                        var pdfPath = "${Environment.getExternalStorageDirectory()}" +
                                "/PDFFiles/${fileName}"
                        startActivity(Intent(this@ImagesActivity,
                            PDFViewActivity::class.java).apply {
                                putExtra("pdf_path",pdfPath
                                  )
                        })
                        finish()
                    }
                }

            }
        }
    }

    fun getImageDirectories(mContext: Context): ArrayList<String>? {

        val contentResolver = mContext.contentResolver
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.DATA
        )
        val includeImages = MediaStore.Images.Media.MIME_TYPE + " LIKE 'image/%' "
        val excludeGif =
            " AND " + MediaStore.Images.Media.MIME_TYPE + " != 'image/gif' " + " AND " + MediaStore.Images.Media.MIME_TYPE + " != 'image/giff' "
        val selection = includeImages + excludeGif
        val cursor = contentResolver.query(queryUri, projection, selection, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val photoUri = cursor.getString(cursor.getColumnIndex(projection[0]))
                if (!directories.contains(File(photoUri).parent)) {
                    directories.add(File(photoUri).parent)
                }
            } while (cursor.moveToNext())
        }
        return directories
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.d("Select Folder >>", "${directories[p2]}")
        var directoryName = ""
            directoryName = directories.get(p2)
        imageAdaptor.setList(RetrivePhoto(directoryName))
        binding.rv.adapter = imageAdaptor
        imageAdaptor.notifyDataSetChanged()
        binding.tvDeselect.setOnClickListener {
            deselectALl()
        }
        imageAdaptor.itemClick = {
            if (it.isSelected) {
                selectedImages.add(it)
            } else {
                selectedImages.remove(it)
            }
            setUserSelected()
        }


    }

    private fun setUserSelected() {
        if (selectedImages.isNotEmpty()) {
            binding.llBottom.visibility = View.VISIBLE
            var selectAdapter = UserSelectImageAdapter(selectedImages, this)
            binding.rvSelected.adapter = selectAdapter
            binding.tvSelectedCount.text = "Selected: ${selectedImages.size}"
            selectAdapter.notifyDataSetChanged()

            selectAdapter.removeClick = {
                selectedImages.remove(it)
                selectAdapter.notifyDataSetChanged()
                if (selectedImages.size == 0) {
                    binding.llBottom.visibility = View.GONE
                }
            }

        } else {
            binding.llBottom.visibility = View.GONE
        }


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        setUserSelected()
    }

    fun deselectALl() {
        selectedImages.removeAll {
            it.isSelected
        }
        imageAdaptor.notifyDataSetChanged()
        setUserSelected()
    }
}