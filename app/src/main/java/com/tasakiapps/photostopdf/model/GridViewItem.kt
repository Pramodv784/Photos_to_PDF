package com.tasakiapps.photostopdf.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class GridViewItem(
    val title:String,
    val path:String,
    val size:Long?,
    var isSelected:Boolean = false
):Serializable{

    companion object {

        val DIFF = object : DiffUtil.ItemCallback<GridViewItem>() {
            override fun areItemsTheSame(oldItem: GridViewItem, newItem: GridViewItem): Boolean {
                return oldItem.path == newItem.path
            }

            override fun areContentsTheSame(oldItem: GridViewItem, newItem: GridViewItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
