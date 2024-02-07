package com.tasakiapps.photostopdf

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasakiapps.photostopdf.model.GridViewItem
import com.tasakiapps.photostopdf.repo.GalleryRepository
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
    fun retiveDirectory(context: Context){
         folderList.removeAll{it is String}
        viewModelScope.launch {
            runCatching {
                repository.provideDirectory(context)
            }.fold({
               it.let {
                   folderList.addAll(it!!)
                   folderLiveData.value = Pair(false,folderList)
               }
            },
                {
                    folderLiveData.value = Pair(false,folderList)
                })

        }
    }
   fun retrivePhoto(folderName:String){
       photoList.removeAll { it is GridViewItem }
       viewModelScope.launch {
           runCatching {
             repository.provideGalleryPhotoList(folderName)
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
        listOfPhotoSelection.removeIf{photo.path == it.path}
        photoSelectionLiveData.value = Pair(false, listOfPhotoSelection.toMutableList())
    }
}