package com.tasakiapps.photostopdf.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RadioButton
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.tasakiapps.photostopdf.ImageViewModel
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.adaptor.DragItemTouchHelper
import com.tasakiapps.photostopdf.adaptor.SelectedImageAdapter
import com.tasakiapps.photostopdf.databinding.ActivitySelectedImageBinding
import com.tasakiapps.photostopdf.databinding.ConvertDialogBinding
import com.tasakiapps.photostopdf.databinding.OrientationViewBinding
import com.tasakiapps.photostopdf.model.GridViewItem
import com.tasakiapps.photostopdf.utils.ImageToPDF
import com.tasakiapps.photostopdf.utils.ImageUtil.convertUrlToPath
import com.tasakiapps.photostopdf.utils.Utils.dismissProgressDialog
import com.tasakiapps.photostopdf.utils.Utils.showProgressDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileDescriptor
import java.io.IOException


class SelectedImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectedImageBinding
    private lateinit var viewModel: ImageViewModel
    private lateinit var adaptor: SelectedImageAdapter
    private var seletedImageList: ArrayList<GridViewItem> = ArrayList()
    val REQUEST_IMAGE_CAPTURE = 1
    var image_uri: Uri? = null
    private var fileName = ""
    private var isOrientation = ""




    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
                if (it.getResultCode() === RESULT_OK) {
                    var galleryImage = it.data?.data
                    val gallerImagePath = convertUrlToPath(this, galleryImage!!)
                    Log.d("Gallery Image>>>", "$gallerImagePath")
                    seletedImageList.add(GridViewItem("", gallerImagePath!!, 0, false))
                    adaptor.setList(seletedImageList)
                    adaptor.notifyDataSetChanged()
                    //  val inputImage = uriToBitmap(image_uri!!)
                    //  val rotated = rotateBitmap(inputImage!!)
                    // imageView.setImageBitmap(rotated)
                }
            }
        )

    //TODO capture the image using camera and display it
    private var cameraActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
                if (it.resultCode === RESULT_OK) {

                    var cameraImagePath = convertUrlToPath(this, image_uri!!)
                    seletedImageList.add(GridViewItem("", cameraImagePath!!, 0, false))
                    Log.d("Camera Image>>>", "$cameraImagePath")
                    adaptor.setList(seletedImageList)
                    adaptor.notifyDataSetChanged()
                    // val inputImage = uriToBitmap(image_uri!!)
                    //  val rotated = rotateBitmap(inputImage!!)
                    // imageView.setImageBitmap(rotated)
                }
            }
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        initViews()
    }

    private fun initViews() {
        var bundle = intent.extras
        seletedImageList.clear()
        seletedImageList = bundle?.getSerializable("bundle") as ArrayList<GridViewItem>

        Log.d("Selected ImageSize>>>>", "${seletedImageList.size}")

        adaptor = SelectedImageAdapter(this)
        adaptor.setList(seletedImageList)
        binding.rv.adapter = adaptor


        val dragItemTouchHelper = DragItemTouchHelper(adaptor,this,binding.rv)

        val itemTouchHelper = ItemTouchHelper(dragItemTouchHelper)
        itemTouchHelper.attachToRecyclerView(binding.rv)

        binding.Ivcamera.setOnClickListener {
            if (isCameraPermissionGranted()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }
        binding.Ivgallery.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryActivityResultLauncher.launch(galleryIntent)
        }
        binding.pdfBT.setOnClickListener {
            convertDialog()
        }
        binding.Ivorientation.setOnClickListener {

            orientationDialog()

        }


    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        cameraActivityResultLauncher.launch(cameraIntent)
    }

    private fun requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
            ) {
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            openCamera()
        }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //binding.imgViewer.setImageBitmap(imageBitmap)
        }
    }

    @SuppressLint("Range")
    fun rotateBitmap(input: Bitmap): Bitmap? {
        val orientationColumn =
            arrayOf(MediaStore.Images.Media.ORIENTATION)
        val cur: Cursor? = contentResolver.query(image_uri!!, orientationColumn, null, null, null)
        var orientation = -1
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]))
        }
        Log.d("tryOrientation", orientation.toString() + "")
        val rotationMatrix = Matrix()
        rotationMatrix.setRotate(orientation.toFloat())
        return Bitmap.createBitmap(input, 0, 0, input.width, input.height, rotationMatrix, true)
    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }



   private fun orientationDialog(){
       var selectedOption =""
       val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
           .create()
       val dialogBinding = OrientationViewBinding.inflate(layoutInflater)
       builder.setView(dialogBinding.root)
       dialogBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
           val radioButton: RadioButton = group.findViewById(checkedId)
            selectedOption = radioButton.text.toString()
         //  Toast.makeText(this, "Selected option: $selectedOption", Toast.LENGTH_SHORT).show()


           Log.d("Radio Button >>","${radioButton.text}")
       }
       dialogBinding.apply.setOnClickListener {
         builder.dismiss()
           if(selectedOption.equals("Horizontal")){
               isOrientation = "Horizontal"
               val layoutManager = GridLayoutManager(this, 2)
               binding.rv.layoutManager = layoutManager
               adaptor.changeToLandscape(true)
           }
           else{
               adaptor.changeToLandscape(false)
               isOrientation = "Vertical"
               val layoutManager = GridLayoutManager(this, 3)
               binding.rv.layoutManager = layoutManager
           }

           // Assigning id of the checked radio button

       }
       dialogBinding.btCancel.setOnClickListener { builder.dismiss() }
       builder.setCanceledOnTouchOutside(false)
       builder.show()
   }

   private fun convertDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val dialogBinding = ConvertDialogBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        /* button.setOnClickListener {
             builder.dismiss()
         }*/

        var imageQuality = 100

        dialogBinding.etFileName.setText("File${System.currentTimeMillis()}")

        dialogBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton =group.findViewById<RadioButton>(checkedId)
            val selectedText = selectedRadioButton.getText().toString()
            // Do something with the selected radio button
            // For example, display a toast with the selected option
            imageQuality = when (selectedText) {
                "Low" -> 25
                "Medium" -> 50
                "High" -> 100
                else -> 100
            }
            //Toast.makeText(this, "Selected: $selectedText", Toast.LENGTH_SHORT).show()
        }

        dialogBinding.convertBT.setOnClickListener {

            fileName = dialogBinding.etFileName.text.toString()
            generatePDF(fileName, imageQuality,builder)
            Log.d("File Name>>>", "$fileName")
        }

        dialogBinding.btCancel.setOnClickListener { builder.cancel() }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun generatePDF(fileName: String, quality: Int, builder: AlertDialog) {
        showProgressDialog(this@SelectedImageActivity, "Generating PDF...")
        val imagePaths = ArrayList<String>()

        seletedImageList.forEach {
            imagePaths.add(it.path)
        }
        val converter = ImageToPDF(this)
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("PDF File Name Time>>>", "${System.currentTimeMillis()}")
            converter.createPdfWithMultipleImages(
                imagePaths,
                "$fileName.pdf",quality, isOrientation
            )
        }
        converter.pdfCallback = { status, fileName ->

            if (status) {
                dismissProgressDialog()
                Log.d("PDF File Name >>>", "${fileName}")
                var pdfPath = "${Environment.getExternalStorageDirectory()}" +
                        "/PDFFiles/${fileName}"
                startActivity(Intent(
                    this@SelectedImageActivity,
                    PDFViewActivity::class.java
                ).apply {
                    putExtra(
                        "pdf_path", pdfPath
                    )
                    putExtra("destination_home",true)
                })
                finish()
            }
        }

    }

}