package com.tasakiapps.photostopdf

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tasakiapps.photostopdf.adaptor.PDFAdapter
import com.tasakiapps.photostopdf.databinding.FragmentCreatedpdfBinding
import com.tasakiapps.photostopdf.utils.PDFUtils.getExternalPDFFileList

class CreatedpdfFragment : Fragment() {
    private lateinit var binding:FragmentCreatedpdfBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreatedpdfBinding.inflate(layoutInflater)
        var viewStatus = arguments?.getBoolean("Key")


        initView()
        return binding.root
    }


    private fun initView() {

        binding.rvPdf.adapter = PDFAdapter(getExternalPDFFileList(requireContext())!!, requireContext())

    }


}