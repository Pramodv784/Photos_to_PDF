package com.tasakiapps.photostopdf

import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.tasakiapps.photostopdf.adaptor.UserSelectImageAdapter
import com.tasakiapps.photostopdf.databinding.ActivityImagesBinding
import com.tasakiapps.photostopdf.extension.RetrivePhoto
import com.tasakiapps.photostopdf.model.GridViewItem
import java.io.File


class ImagesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityImagesBinding
    val directories = ArrayList<String>()
    private var selectedImages = ArrayList<GridViewItem>()
    private lateinit var adapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.spinner.onItemSelectedListener = this

        var listFolder = ArrayList<String>()
        getImageDirectories(this)?.forEach {
            var folderName = it.split("/").last()
            listFolder.add(folderName)
        }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            listFolder
        )
        binding.spinner.adapter = adapter


        getImageDirectories(this@ImagesActivity)?.forEach {
            Log.d("Images >>>", "${it}")
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
        val listImage = directories.get(p2)
        adapter = ImageAdapter(this@ImagesActivity.RetrivePhoto(listImage).reversed(), this)
        binding.rv.adapter = adapter
        adapter.notifyDataSetChanged()

        adapter.itemClick = {
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
            selectAdapter.notifyDataSetChanged()

            selectAdapter.removeClick={
                selectedImages.remove(it)
                selectAdapter.notifyDataSetChanged()
            }

        } else {
            binding.llBottom.visibility = View.GONE
        }




    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        setUserSelected()
    }


}