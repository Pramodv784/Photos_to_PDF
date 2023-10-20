package com.tasakiapps.photostopdf.utils

import java.io.File
import java.io.FileFilter

class ImageFilter : FileFilter {
    override fun accept(file: File): Boolean {
        if (file.isDirectory) {
            return true
        } else if (isImageFile(file.absolutePath)) {
            return true
        }
        return false
    }
    fun isImageFile(filePath: String): Boolean {
        return filePath.endsWith(".jpg") || filePath.endsWith(".png")
    }
}