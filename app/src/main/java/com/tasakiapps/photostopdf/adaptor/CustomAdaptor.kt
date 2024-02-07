package com.tasakiapps.photostopdf.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.tasakiapps.photostopdf.R

class CustomPagerAdapter(private val context: Context, private val tabTitles: List<String>) : PagerAdapter() {

    override fun getCount(): Int {
        return tabTitles.size
    }

    @SuppressLint("MissingInflatedId")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.tab_pager_item, container, false)

        val tabIcon = view.findViewById<ImageView>(R.id.tabIcon)
        val tabText = view.findViewById<TextView>(R.id.tabText)

        // Customize the tab item here based on position if needed

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}