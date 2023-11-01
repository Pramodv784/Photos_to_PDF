package com.tasakiapps.photostopdf.adaptor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.databinding.PdfViewBinding
import com.tasakiapps.photostopdf.model.GridViewItem
import com.tasakiapps.photostopdf.model.PdfModel
import com.tasakiapps.photostopdf.ui.PDFViewActivity
import com.tasakiapps.photostopdf.utils.GetThumbnail
import java.io.File


class PDFAdapter(val list:List<PdfModel>, val context:Context) :RecyclerView.Adapter<PDFAdapter.ViewHolder>() {
   lateinit var itemClick:(item:GridViewItem) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
      val binding  = PdfViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemData = list[position]
        with(holder){
            binding.tvFile.text = itemData.fileName
              if(!GetThumbnail.isPdfPasswordProtected(itemData.uri)){
                  binding.ivPdf.setImageBitmap(GetThumbnail.
                  generateThumbnailFromPdf(context,File(itemData.uri)))
              }



            binding.root.setOnClickListener {
                context.startActivity(Intent(context,PDFViewActivity::class.java)
                    .putExtra("pdf_path", File(itemData.uri).absolutePath))
            }
        }



    }

    override fun getItemCount(): Int {
      return  list.size
    }

  inner  class ViewHolder(val binding: PdfViewBinding):RecyclerView.ViewHolder(binding.root)
}