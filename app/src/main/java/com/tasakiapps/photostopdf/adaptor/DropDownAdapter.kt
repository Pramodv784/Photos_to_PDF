package com.tasakiapps.photostopdf.adaptor


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tasakiapps.photostopdf.R


class DropDownAdapter(val context: Context,var folderList: List<String> = listOf()) :
   BaseAdapter() {


    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row: View = inflater.inflate(R.layout.item_spinner, parent, false)
        val label = row.findViewById<TextView>(R.id.txt) as TextView
        label.text = folderList[position]

        return row
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View? {
        return getCustomView(position, convertView, parent)
    }

    override fun getCount(): Int {
       return folderList.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
       return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var vista = convertView

        // layout for spinner widget


        // layout for spinner widget
        if (vista == null) {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            vista = inflater.inflate(R.layout.ispinner_view, null)

            val label = vista.findViewById<TextView>(R.id.txt) as TextView
            label.text = folderList[position]
        }

        return vista
    }





}