package com.tasakiapps.photostopdf

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tasakiapps.photostopdf.databinding.ActivityMainBinding
import com.tasakiapps.photostopdf.ui.PDFActivity
import com.tasakiapps.photostopdf.utils.CustomDialog.showAlert


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        initViews()
        setContentView(binding.root)


    }

    private fun initViews() {
        if(!checkPermission())requestPermission()
        binding.convertBT.setOnClickListener {
          if(!checkPermission()){
              requestPermission()
          }
            else{
                startActivity(Intent(this@MainActivity,ImagesActivity::class.java))
              Log.d("Permissio Granted","*****")
            }
        }
        binding.btFile.setOnClickListener {
            startActivity(Intent(this@MainActivity,PDFActivity::class.java))
        }
    }

    fun requestPermission(){
        showAlert(this@MainActivity){
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            } else {
                //below android 11=======
                //   startActivity(Intent(this, ActivityPicture::class.java))
                ActivityCompat.requestPermissions(this, arrayOf<String>(WRITE_EXTERNAL_STORAGE), 100)
            }
        }
    }
    private fun checkPermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(this@MainActivity, READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(this@MainActivity, WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

    val requestPhotoPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                when (it.key) {
                    Manifest.permission.CAMERA -> {
                        if (it.value) {
                          //  takeImage()
                        } else {
                            Toast.makeText(
                             this@MainActivity,
                                "Camera permission is denied",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                        if (it.value) {
                          //  startActivities()
                        } else {
                            Toast.makeText(
                               this@MainActivity,
                                "Read storage permission is denied",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
}