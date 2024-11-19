package com.tasakiapps.photostopdf.repo

import android.content.Context

import com.tasakiapps.photostopdf.extension.RetrivePhoto
import com.tasakiapps.photostopdf.extension.retrievePhotos
import com.tasakiapps.photostopdf.model.GridViewItem
import com.tasakiapps.photostopdf.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GalleryRepository {

suspend fun provideGalleryPhotoList(context:Context,foldername:String):List<GridViewItem>{
   return withContext(Dispatchers.IO){
           RetrivePhoto(context,foldername)
   }
}

    suspend fun provideAllImages(context:Context):List<GridViewItem>{

        return withContext(Dispatchers.IO){
            context.retrievePhotos()
        }
    }

    suspend fun provideDirectory(context:Context):List<String>?{
        return withContext(Dispatchers.IO){
            Utils.getImageDirectories(context)
        }
    }
}