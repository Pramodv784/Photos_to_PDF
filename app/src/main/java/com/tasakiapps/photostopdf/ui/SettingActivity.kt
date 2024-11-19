package com.tasakiapps.photostopdf.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.databinding.ActivitySettingBinding
import com.tasakiapps.photostopdf.databinding.FileLocationBinding
import com.tasakiapps.photostopdf.databinding.OrientationViewBinding
import com.tasakiapps.photostopdf.extension.changeStatusBarColor

class SettingActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
  binding.cvFileSave.setOnClickListener { fileLocationDialog() }

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
}