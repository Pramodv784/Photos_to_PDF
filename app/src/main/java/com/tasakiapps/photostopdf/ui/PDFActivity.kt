package com.tasakiapps.photostopdf.ui
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.tabs.TabLayout
import com.tasakiapps.photostopdf.CreatedpdfFragment
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.adaptor.CustomPagerAdapter
import com.tasakiapps.photostopdf.adaptor.ViewPagerAdapter
import com.tasakiapps.photostopdf.databinding.ActivityPdfactivityBinding
import com.tasakiapps.photostopdf.extension.changeStatusBarColor
import com.tasakiapps.photostopdf.utils.ImageUtil


class PDFActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfactivityBinding
    lateinit var navController : NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        this.changeStatusBarColor(R.color.color_background)
        binding.back.setOnClickListener { onBackPressed() }

        appBarConfiguration = AppBarConfiguration(setOf(R.id.createdpdfFragment,
            R.id.pdfFragment))

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(CreatedpdfFragment.getInstance(true), "PDF Converted")
        adapter.addFragment(CreatedpdfFragment.getInstance(false), "PDF Reader")


        binding.viewPager.adapter = adapter

        binding.tabLayout2.setupWithViewPager(binding.viewPager)

    }

}