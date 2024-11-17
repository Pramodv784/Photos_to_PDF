package com.tasakiapps.photostopdf

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tasakiapps.photostopdf.adaptor.DropDownAdapter
import com.tasakiapps.photostopdf.adaptor.UserSelectImageAdapter
import com.tasakiapps.photostopdf.databinding.ActivityImagesBinding
import com.tasakiapps.photostopdf.extension.changeStatusBarColor
import com.tasakiapps.photostopdf.model.GridViewItem
import com.tasakiapps.photostopdf.ui.PDFViewActivity
import com.tasakiapps.photostopdf.ui.SelectedImageActivity
import com.tasakiapps.photostopdf.utils.ImageToPDF
import com.tasakiapps.photostopdf.utils.Keys.IMAGE_LIST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.Serializable


class ImagesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityImagesBinding
    val directories = ArrayList<String>()
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
            Log.e("Pramod", "observer: "+listFolder.toString() )
          //  listFolder.sortedWith(  compareBy(String.CASE_INSENSITIVE_ORDER,{it}))

           dropDownAdapter = DropDownAdapter(this,listFolder.toMutableList())



        /*    val spinnerAdaptor = ArrayAdapter(this@ImagesActivity,android.R.layout.simple_spinner_item,listFolder.toMutableList())

            spinnerAdaptor.setDropDownViewResource(R.layout.item_spinner)*/



            binding.spinner.adapter = dropDownAdapter
        }

        viewModel.photoLiveData.observe(this) {
            imageAdaptor.setList(it.second)
            binding.rv.adapter = imageAdaptor
            imageAdaptor.notifyItemChanged(0)
        }

        viewModel.photoSelectionLiveData.observe(this) {

            if (it.second.isNotEmpty()) {
                Log.d("selected list ","${it.second.size}")
                bottomAdaptor.setList(it.second.toMutableList())
                binding.llBottom.visibility = View.VISIBLE
                binding.rvSelected.adapter = bottomAdaptor
                binding.tvSelectedCount.text = "Selected: ${it.second.size}"
                bottomAdaptor.notifyDataSetChanged()
                if (it.first) {
                    it.second.lastOrNull().let { photo ->

                        Log.d("Selected Item>>",photo?.path.toString())
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

      //  viewModel.getAllImages(this)

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

            if (viewModel.photoSelectionLiveData.value?.second?.isNotEmpty() == true) {
                var bundle = Bundle()
                bundle.putSerializable("bundle",viewModel.photoSelectionLiveData.value?.second as Serializable)
                startActivity(Intent(this@ImagesActivity,
                    SelectedImageActivity::class.java)
                    .putExtras(bundle))


            }
            else{
                Toast.makeText(this,"Please Select Image",Toast.LENGTH_SHORT).show()
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
            viewModel.onPhotoSelected(photo, 20)
        }
        imageAdaptor.notifyDataSetChanged()


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        viewModel.retrivePhoto(directories[p2])
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}