package com.tasakiapps.photostopdf.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.databinding.GridViewItemBinding
import com.tasakiapps.photostopdf.databinding.SelectedImageViewBinding
import com.tasakiapps.photostopdf.model.GridViewItem
import com.tasakiapps.photostopdf.utils.Utils
import java.util.Collections


class SelectedImageAdapter(val context: Context, private var list: List<GridViewItem> = listOf()
,  private val onItemMovedCallback: (List<GridViewItem>) -> Unit,private var itemClick:(Int) -> Unit) :
    RecyclerView.Adapter<SelectedImageAdapter.ViewHolder>(), ItemTouchHelperAdapter{

    private val selectedItems = mutableSetOf<String>()
    private var llparent:LinearLayout?=null

    val counterMap = mutableMapOf<String, Int>()
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding =
            SelectedImageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setList(imageList: List<GridViewItem>) {
        list = imageList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemData = list[position]
        with(holder) {
            Log.d("Photo Bitmap>>> ", "${itemData}")
            binding.countText.text = ""+(position+1)
            Glide.with(context).load(itemData.path).placeholder(R.drawable.ic_home_bg)
                .into(binding.itemImage)
       //  binding.itemImage.setImageBitmap(itemData.filebitmap)







            binding.itemImage.setOnClickListener { itemClick.invoke(position)}
        }


    }


    public fun changeToLandscape(status:Boolean){
        if(status){
            llparent?.layoutParams?.height = 100
        }
        else{
            llparent?.layoutParams?.height = 150
        }
        notifyDataSetChanged()

    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: SelectedImageViewBinding) : RecyclerView.ViewHolder(binding.root),View.OnLongClickListener{

        init {
            itemView.setOnLongClickListener(this)
        }
        override fun onLongClick(p0: View?): Boolean {
            return true
        }

    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)

        onItemMovedCallback(list)
    }

}
interface ItemTouchHelperAdapter {
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}