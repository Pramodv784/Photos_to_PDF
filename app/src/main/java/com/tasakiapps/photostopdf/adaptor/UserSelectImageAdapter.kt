package com.tasakiapps.photostopdf.adaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.databinding.SelectedItemViewBinding
import com.tasakiapps.photostopdf.model.GridViewItem


class UserSelectImageAdapter(val list: List<GridViewItem>, val context: Context) :
    RecyclerView.Adapter<UserSelectImageAdapter.ViewHolder>() {
    lateinit var itemClick: (item: GridViewItem) -> Unit
    lateinit var removeClick: (item: GridViewItem) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding =
            SelectedItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemData = list[position]

        with(holder) {
            Log.d("Photo Item>>> ", "${itemData.title}")
            Glide.with(context).load(itemData.path).placeholder(R.drawable.ic_home_bg)
                .into(binding.itemImage)
            binding.itemImage.setOnClickListener {
                if (!itemData.isSelected) {
                    itemData.isSelected = true
                    binding.tvCount.visibility = View.VISIBLE
                    itemClick.invoke(itemData)
                } else {
                    itemData.isSelected = false
                    binding.tvCount.visibility = View.GONE
                    removeClick.invoke(itemData)
                }
            }
            binding.tvCount.setOnClickListener { removeClick.invoke(itemData) }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: SelectedItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)
}