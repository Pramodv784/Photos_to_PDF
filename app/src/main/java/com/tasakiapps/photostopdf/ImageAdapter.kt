package com.tasakiapps.photostopdf

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tasakiapps.photostopdf.databinding.GridViewItemBinding
import com.tasakiapps.photostopdf.model.GridViewItem


class ImageAdapter(val context: Context, private var list: List<GridViewItem> = listOf()) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    lateinit var itemClick: (item: String) -> Unit
    private val selectedItems = mutableSetOf<String>()

    val counterMap = mutableMapOf<String, Int>()
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

            binding.tvCount.text = counter.toString()

            if (counter > 0) {
                binding.tvCount.visibility = View.VISIBLE
                binding.lConst.setBackgroundResource(R.drawable.red_stroke)
            } else {
                binding.tvCount.visibility = View.GONE
                binding.lConst.setBackgroundResource(0)
            }
            binding.itemImage.setOnClickListener {
                itemClick.invoke(itemData.path)
            }
        }


    }

    fun decreaseAllValueOnMap(value: Int) {
        counterMap.forEach { kv ->
            if (kv.value > value) {
                counterMap[kv.key] = counterMap[kv.key]?.minus(1) ?: 0
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: GridViewItemBinding) : RecyclerView.ViewHolder(binding.root)
}