package com.tasakiapps.photostopdf

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tasakiapps.photostopdf.databinding.GridViewItemBinding
import com.tasakiapps.photostopdf.model.GridViewItem
import java.util.concurrent.CountDownLatch


class ImageAdapter(val context: Context, private var list: List<GridViewItem> = listOf()) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    lateinit var itemClick: (item: String) -> Unit
    private val selectedItems = mutableSetOf<String>()
    private lateinit var tvCount:TextView
    private lateinit var lconst:ConstraintLayout



    var counterMap = mutableMapOf<String, Int>()
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding =
            GridViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    fun setList(imageList: List<GridViewItem>) {
        list = imageList
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemData = list[position]
        with(holder) {
            Log.d("Photo Item>>> ", "${itemData.path}")
            Glide.with(context).load(itemData.path).placeholder(R.drawable.ic_home_bg)
                .into(binding.itemImage)

            val counter = counterMap[itemData.path] ?: 0

            tvCount = binding.tvCount
            lconst = binding.lConst


            Log.d("Counter  Value>>> ", "${counter}")
            binding.tvCount.text = counter.toString()
            isViewVisible(counter>0)
            binding.itemImage.setOnClickListener {
                itemClick.invoke(itemData.path)
            }
        }


    }



     fun isViewVisible( isVisible:Boolean){
        if(isVisible){
            tvCount.visibility = View.VISIBLE
            lconst.setBackgroundResource(R.drawable.red_stroke)
        }
        else{
            tvCount.visibility = View.GONE
            lconst.setBackgroundResource(0)
        }
    }

    fun decreaseAllValueOnMap(value: Int) {
        counterMap.forEach { kv ->
            if (kv.value > value) {
                counterMap[kv.key] = counterMap[kv.key]?.minus(1) ?: 1
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: GridViewItemBinding) : RecyclerView.ViewHolder(binding.root)
}