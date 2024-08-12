package com.tasakiapps.photostopdf

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tasakiapps.photostopdf.adaptor.PDFAdapter
import com.tasakiapps.photostopdf.databinding.FragmentCreatedpdfBinding
import com.tasakiapps.photostopdf.utils.PDFUtils.getAllPdfFiles
import com.tasakiapps.photostopdf.utils.PDFUtils.getExternalPDFFileList

class CreatedpdfFragment : Fragment() {
    private lateinit var binding:FragmentCreatedpdfBinding
    private  var viewStatus:Boolean = false

    companion object {

        /**
         * Method used to get the instance of the fragment.
         *
         * @return fragment instance
         */
        @JvmStatic
        fun getInstance(state: Boolean): CreatedpdfFragment {
            val fragment = CreatedpdfFragment()
            val bundle = Bundle()
            bundle.putBoolean("viewState", state)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreatedpdfBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }


    private fun initView() {
        if(arguments!=null){
            viewStatus = arguments?.getBoolean("viewState")?:false
        }

        Log.d("view status>>>","$viewStatus")
        if(viewStatus){
            binding.rvPdf.adapter = getAllPdfFiles("${Environment.getExternalStorageDirectory()}"+"/PDFFiles/")
                ?.let { PDFAdapter(it, requireContext()) }
        }
        else{
            binding.rvPdf.adapter = PDFAdapter(getExternalPDFFileList(requireContext())!!
                , requireContext())
        }



    }


}