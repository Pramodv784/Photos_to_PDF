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
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.adaptor.CustomPagerAdapter
import com.tasakiapps.photostopdf.databinding.ActivityPdfactivityBinding
import com.tasakiapps.photostopdf.extension.changeStatusBarColor


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


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_bottem_navigation) as NavHostFragment?
         navController = navHostFragment!!.navController

        val tabTitles = listOf("Tab 1", "Tab 2", "Tab 3")
        val customPagerAdapter = CustomPagerAdapter(this, tabTitles)
        binding.viewPager.adapter = customPagerAdapter
        var tab:TabLayout= binding.tabLayout

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        tab.setupWithNavController(navController)


        // Customize tabs if needed
        for (i in 0 until   binding.tabLayout.tabCount) {
            val tab =   binding.tabLayout.getTabAt(i)
            tab?.customView = customPagerAdapter.instantiateItem(  binding.tabLayout, i) as View
        }


        appBarConfiguration = AppBarConfiguration(setOf(R.id.createdpdfFragment,
            R.id.pdfFragment))
       // NavigationUI.setupWithNavController(appBarConfiguration,navController)

      //  binding.rvPdf.adapter = PDFAdapter(list?.reversed()!!, this@PDFActivity)
           var bundle = Bundle()

     /*   binding.tvPdfConverted.setOnClickListener {
            bundle.putBoolean("Key",true) /// Converted PDF
            navController.navigate(R.id.createdpdfFragment,bundle)
            changeBackground(true, binding.tvPdfConverted, binding.tvPdfReader)

        }
        binding.tvPdfReader.setOnClickListener {
            bundle.putBoolean("Key",false) // All the PDF files
            navController.navigate(R.id.createdpdfFragment,bundle)
            changeBackground(false, binding.tvPdfConverted, binding.tvPdfReader)
        }
*/
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,appBarConfiguration)
    }
    fun changeBackground(
        isBackGround: Boolean, firstView: AppCompatTextView, secondView: AppCompatTextView
    ) {
        if (isBackGround) {
            firstView.apply {
                this.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@PDFActivity, R.color.color_text_red
                    )
                );
                setTextColor(ContextCompat.getColor(this@PDFActivity, R.color.white))

                secondView.apply {
                    backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@PDFActivity, R.color.color_grey
                        )
                    )
                    setTextColor(ContextCompat.getColor(this@PDFActivity, R.color.color_text_grey))

                }


            }
        } else {
            secondView.apply {
                this.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@PDFActivity, R.color.color_text_red
                    )
                );
                setTextColor(ContextCompat.getColor(this@PDFActivity, R.color.white))

                firstView.apply {
                    backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@PDFActivity, R.color.color_grey
                        )
                    )
                    setTextColor(ContextCompat.getColor(this@PDFActivity, R.color.color_text_grey))

                }


            }
        }
    }
}