package com.tasakiapps.photostopdf.viewmodel

import android.content.Context
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasakiapps.photostopdf.model.PdfModel
import com.tasakiapps.photostopdf.utils.PDFUtils.getAllPdfFiles
import com.tasakiapps.photostopdf.utils.PDFUtils.getExternalPDFFileList
import kotlinx.coroutines.launch

class PDFViewModel :ViewModel() {

    private var pdfCreateList = MutableLiveData(ArrayList<PdfModel>())

    val _pdfCreateList get() = pdfCreateList


    fun getPDFCreated(){
        viewModelScope.launch {
          var data =  getAllPdfFiles("${Environment.getExternalStorageDirectory()}"+"/PDFFiles/")
         data.let { pdfList->
             pdfCreateList.postValue(pdfList)
         }
        }
    }

    fun getAllPDFFile(context:Context){
        viewModelScope.launch {
            var data = getExternalPDFFileList(context)
            data.let { pdfList->
                pdfCreateList.postValue(pdfList)
            }
        }
    }
}