package com.tasakiapps.photostopdf

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tasakiapps.photostopdf.databinding.GridViewItemBinding
import com.tasakiapps.photostopdf.model.GridViewItem


class ImageAdapter(val context:Context,private var list:List<GridViewItem> = listOf()) :RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
   lateinit var itemClick:(item:GridViewItem) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
      val binding = GridViewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

     fun  setList( imageList:List<GridViewItem>){
        list = imageList
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemData = list[position]
        with(holder){

            Log.d("Photo Item>>> ","${itemData.title}")
            Glide.with(context).load(itemData.path).placeholder(R.drawable.ic_home_bg)
                .into(binding.itemImage)
            binding.itemImage.setOnClickListener {
                if(!itemData.isSelected){
                    itemData.isSelected = true
                  binding.tvCount.visibility = View.VISIBLE
                    binding.lConst.setBackgroundResource(R.drawable.red_stroke)
                    itemClick.invoke(itemData)
                }
                else{
                    itemData.isSelected = false
                    binding.tvCount.visibility = View.GONE
                    itemClick.invoke(itemData)
                    binding.lConst.setBackgroundResource(0)
                }
            }
        }



    }

    override fun getItemCount(): Int {
      return  list.size
    }

    inner class ViewHolder(val binding:GridViewItemBinding):RecyclerView.ViewHolder(binding.root)
}