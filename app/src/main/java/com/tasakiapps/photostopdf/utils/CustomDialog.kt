package com.tasakiapps.photostopdf.utils


import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.tasakiapps.photostopdf.R
import org.w3c.dom.Text


object CustomDialog {


        private var alertDialog: AlertDialog? = null

        fun showAlert(context: Activity,onClick:()-> Unit) {
            val dialog = AlertDialog.Builder(context,R.style.CustomAlertDialog)
                .setView(R.layout.dialog_permission)
                .create()
            dialog.show()
            var cancelBt  = dialog.findViewById<TextView>(R.id.btCancel)
            var grantBt  = dialog.findViewById<MaterialButton>(R.id.btallow)

            cancelBt?.setOnClickListener { dialog.dismiss() }
            grantBt?.setOnClickListener {
                Log.d("OnClick","*****")
                dialog.dismiss()
                onClick.invoke()
            }




        }

        fun dismiss() {
            alertDialog?.dismiss()
        }


}