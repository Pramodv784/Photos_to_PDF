package com.tasakiapps.photostopdf.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.extension.changeStatusBarColor

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
    }

    private fun initView() {
        this.changeStatusBarColor(R.color.color_background)
    }
}