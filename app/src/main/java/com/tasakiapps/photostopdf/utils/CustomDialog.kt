package com.tasakiapps.photostopdf.utils


import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.tasakiapps.photostopdf.R


object CustomDialog {


        private var alertDialog: AlertDialog? = null

        fun showAlert(context: Activity,onClick:()-> Unit) {
            val dialog = AlertDialog.Builder(context)
                .setView(R.layout.dialog_permission)
                .create()
            dialog.show()
            var cancelBt  = dialog.findViewById<Button>(R.id.bt_cancel)
            var grantBt  = dialog.findViewById<Button>(R.id.bt_grant)

            cancelBt?.setOnClickListener { dialog.dismiss() }
            grantBt?.setOnClickListener {
                Log.d("OnClick","*****")
                onClick.invoke()
            }




        }

        fun dismiss() {
            alertDialog?.dismiss()
        }


}