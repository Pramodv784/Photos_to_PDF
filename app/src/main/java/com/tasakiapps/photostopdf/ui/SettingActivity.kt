package com.tasakiapps.photostopdf.ui

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tasakiapps.photostopdf.BuildConfig
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.databinding.ActivitySettingBinding
import com.tasakiapps.photostopdf.databinding.FileLocationBinding
import com.tasakiapps.photostopdf.utils.Utils.composeEmail

class SettingActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "File Name"
            titleColor = getColor(R.color.black)
            this.setDisplayHomeAsUpEnabled(true)
            this.setHomeAsUpIndicator(R.drawable.back_arrow)

        }
        initView()
    }

    private fun initView() {
        binding.back.setOnClickListener { onBackPressed() }
  binding.cvFileSave.setOnClickListener { fileLocationDialog() }

        binding.feedbackLl.setOnClickListener { feedBack() }

    }

    private fun fileLocationDialog(){

        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val dialogBinding = FileLocationBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)

        dialogBinding.tvLocation.text = "${Environment.getExternalStorageDirectory()}/PDFFiles"

        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }

  fun  feedBack(){
      val builder = StringBuilder()
      builder.append("Device Manufacturer : ")
      builder.append(Build.MANUFACTURER.replace(" ".toRegex(), ""))



      builder.append("\n")
      builder.append("OS Release Version : ")
      builder.append(Build.VERSION.RELEASE)
      builder.append("\n")
      builder.append("SDK : ")
      builder.append(Build.VERSION.SDK_INT)
      builder.append("\n")
      builder.append("Device Id : ")
      builder.append(Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID))
      builder.append("\n")
      builder.append("platform : ")
      builder.append("Android")
      builder.append("\n")
      builder.append("Model : ")
      builder.append(Build.MODEL)


      builder.append("\n")
      composeEmail(this,getString(R.string.support_email),getString(R.string.emailSubject) + " " + BuildConfig.VERSION_NAME, builder.toString())
  }
}