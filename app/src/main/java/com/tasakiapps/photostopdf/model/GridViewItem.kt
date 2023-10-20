package com.tasakiapps.photostopdf.model

data class GridViewItem(
    val title:String,
    val path:String,
    val size:Long?,
    var isSelected:Boolean = false
)
