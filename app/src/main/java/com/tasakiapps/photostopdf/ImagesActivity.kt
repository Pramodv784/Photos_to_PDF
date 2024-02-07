package com.tasakiapps.photostopdf

import android.annotation.SuppressLint
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tasakiapps.photostopdf.adaptor.DropDownAdapter
import com.tasakiapps.photostopdf.adaptor.UserSelectImageAdapter
import com.tasakiapps.photostopdf.databinding.ActivityImagesBinding
import com.tasakiapps.photostopdf.extension.RetrivePhoto
import com.tasakiapps.photostopdf.extension.changeStatusBarColor
import com.tasakiapps.photostopdf.model.GridViewItem
import com.tasakiapps.photostopdf.ui.PDFViewActivity
import com.tasakiapps.photostopdf.utils.ImageToPDF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class ImagesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityImagesBinding
    val directories = ArrayList<String>()
    private var selectedImages = ArrayList<GridViewItem>()
    private lateinit var imageAdaptor: ImageAdapter
    private lateinit var viewModel: ImageViewModel
    private lateinit var bottomAdaptor: UserSelectImageAdapter
    private lateinit var dropDownAdapter: DropDownAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        observer()
        initViews()
    }

    private fun observer() {
        viewModel.folderLiveData.observe(this) {
            val listFolder = ArrayList<String>()
            directories.addAll(it.second)
            it.second.forEach {
                val folderName = it.split("/").last()
                listFolder.add(folderName)
            }

           dropDownAdapter = DropDownAdapter(this,listFolder.toMutableList())

            binding.spinner.adapter = dropDownAdapter
        }

        viewModel.photoLiveData.observe(this) {


            imageAdaptor.setList(it.second)
            binding.rv.adapter = imageAdaptor
            imageAdaptor.notifyItemChanged(0)
        }

        viewModel.photoSelectionLiveData.observe(this) {

            if (it.second.isNotEmpty()) {
                bottomAdaptor.setList(it.second.toMutableList())
                binding.llBottom.visibility = View.VISIBLE
                binding.rvSelected.adapter = bottomAdaptor
                binding.tvSelectedCount.text = "Selected: ${it.second.size}"
                if (it.first) {
                    it.second.lastOrNull().let { photo ->
                        imageAdaptor.counterMap[photo?.path.toString()] =
                            imageAdaptor.counterMap.maxOfOrNull { it.value }?.plus(1) ?: 1

                    }
                }
                imageAdaptor.notifyDataSetChanged()
            } else {
                binding.llBottom.visibility = View.GONE
            }
        }
    }

    private fun initViews() {
        this.changeStatusBarColor(R.color.color_background)
        binding.spinner.onItemSelectedListener = this
        imageAdaptor = ImageAdapter(this)
        bottomAdaptor = UserSelectImageAdapter(this)

        viewModel.retiveDirectory(this)
        imageAdaptor.itemClick = { path ->
            viewModel.photoList.firstOrNull() { it is GridViewItem && it.path.equals(path) }
                .let {
                    if (it is GridViewItem) {
                        onPhotoItemClicked(it)
                    }
                }
        }
        bottomAdaptor.itemClick = {
            onPhotoItemClicked(it)
        }
        binding.pdfBT.setOnClickListener {
            binding.progress.visibility = View.VISIBLE
            if (selectedImages.isNotEmpty()) {
                val imagePaths = ArrayList<String>()
                val converter = ImageToPDF(this)

                selectedImages.forEach {
                    imagePaths.add(it.path)
                }
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("PDF File Name Time>>>", "${System.currentTimeMillis()}")
                    converter.convertImagesToPdf(
                        this@ImagesActivity, imagePaths,
                        "File${System.currentTimeMillis()}.pdf"
                    )
                }
                converter.pdfCallback = { status, fileName ->
                    if (status) {
                        Log.d("PDF File Name >>>", "${fileName}")
                        var pdfPath = "${Environment.getExternalStorageDirectory()}" +
                                "/PDFFiles/${fileName}"
                        startActivity(Intent(
                            this@ImagesActivity,
                            PDFViewActivity::class.java
                        ).apply {
                            putExtra(
                                "pdf_path", pdfPath
                            )
                        })
                        finish()
                    }
                }

            }
        }
    }

    private fun onPhotoItemClicked(photo: GridViewItem) {
        val tag = photo.path
        val mapValue = imageAdaptor.counterMap[tag] ?: 0
        if (mapValue > 0) {
            imageAdaptor.decreaseAllValueOnMap(mapValue)
            imageAdaptor.counterMap[tag] = 0
            viewModel.onPhotoRemoved(photo)
        } else {
            viewModel.onPhotoSelected(photo, 10)
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        viewModel.retrivePhoto(directories[p2])
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}