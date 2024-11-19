package com.tasakiapps.photostopdf

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasakiapps.photostopdf.model.GridViewItem
import com.tasakiapps.photostopdf.repo.GalleryRepository
import com.tasakiapps.photostopdf.utils.ImageUtil.removeIfCompat
import kotlinx.coroutines.launch

class ImageViewModel() :ViewModel() {
    val repository: GalleryRepository = GalleryRepository()

    val photoList = mutableListOf<GridViewItem>()
    val folderList = mutableListOf<String>()

    val photoLiveData = MutableLiveData<Pair<Boolean, List<GridViewItem>>>()
    val folderLiveData = MutableLiveData<Pair<Boolean, List<String>>>()

    //Selectionm
    var listOfPhotoSelection = mutableListOf<GridViewItem>()
    val photoSelectionLiveData = SingleLiveEvent<Pair<Boolean, List<GridViewItem>>>()

    val errorLimitPhotoSelection = SingleLiveEvent<Boolean>()
    val loading = SingleLiveEvent<Boolean>()


    fun getAllImages(context:Context){
        loading.value = true
        photoList.removeAll { it is GridViewItem }
        viewModelScope.launch {
            runCatching {
                repository.provideAllImages(context)
            }.fold({
                loading.value = false
                it.let {
                    photoList.addAll(it)
                    photoLiveData.value =  Pair(false, photoList)

                    Log.d("TAG", "getAllImages: "+it.toString())
                }
            },
                {
                    loading.value = false
                    photoLiveData.value =  Pair(false, photoList)
                })
        }
    }
    fun retiveDirectory(context: Context){
         folderList.removeAll{it is String}
        viewModelScope.launch {
            runCatching {
                repository.provideDirectory(context)
            }.fold({
               it.let { it ->
                   folderList.add("All Images")
                   folderList.addAll(it!!)
                   folderLiveData.value = Pair(false,folderList)
               }
            },
                {
                    folderLiveData.value = Pair(false,folderList)
                })

        }
    }
   fun retrivePhoto(folderName:String,context: Context){
       if(folderName == "All Images"){
           getAllImages(context)
       }
       photoList.removeAll { it is GridViewItem }
       viewModelScope.launch {
           runCatching {
             repository.provideGalleryPhotoList(context,folderName)
           }.fold({
              photoList.addAll(it)
               photoLiveData.value = Pair(false, photoList)
           },{
               photoLiveData.value = Pair(false, photoList)
           })
       }
   }
    fun onPhotoSelected(photo: GridViewItem, limit: Int) {
        if (listOfPhotoSelection.size >= limit) {
          errorLimitPhotoSelection.value = true
        } else {
            listOfPhotoSelection.add(photo)
            photoSelectionLiveData.value = Pair(true, listOfPhotoSelection.toMutableList())
        }
    }
    fun onPhotoRemoved(photo: GridViewItem) {
        listOfPhotoSelection.removeIfCompat{photo.path == it.path}
        photoSelectionLiveData.value = Pair(false, listOfPhotoSelection.toMutableList())
    }
}