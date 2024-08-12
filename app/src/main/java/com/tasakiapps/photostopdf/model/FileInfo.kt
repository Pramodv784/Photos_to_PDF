package com.tasakiapps.photostopdf.model

import java.util.Date

data class FileInfo (
    var fileName:String,
    var fileSize:String,
    var lastModified: String,
    var absolutePath:String
)
