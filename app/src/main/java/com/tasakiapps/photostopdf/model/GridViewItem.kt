package com.tasakiapps.photostopdf.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.tasakiapps.photostopdf.utils.Utils
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
@Parcelize
data class GridViewItem(
    val title:String,
    val path:String,
    val size:Long?,
    var isSelected:Boolean = false,
    var bitmap: Bitmap?  =null,

): Parcelable{


//    var filebitmap  = bitmap
//        get() = fileToBitmap()



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
    private fun fileToBitmap():Bitmap =Utils.filePathToBitmap(path)!!

}
