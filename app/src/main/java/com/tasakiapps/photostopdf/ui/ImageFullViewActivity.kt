package com.tasakiapps.photostopdf.ui

import FullImagePagerAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.databinding.ActivityImageFullViewBinding
import com.tasakiapps.photostopdf.model.GridViewItem

class ImageFullViewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityImageFullViewBinding
    private var seletedImageList: ArrayList<GridViewItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageFullViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }
   private fun initViews(){
       var bundle = intent.extras
       seletedImageList.clear()
       binding.back.setOnClickListener { onBackPressed() }
       seletedImageList = bundle?.getSerializable("bundle") as ArrayList<GridViewItem>

       val position = bundle.getInt("position")

       Log.e("TAG", "initViews:position "+position )

       val adaptor = FullImagePagerAdapter(this,seletedImageList)


       binding.viewPager.adapter = adaptor
       binding.viewPager.setCurrentItem(position, true)




    }
}