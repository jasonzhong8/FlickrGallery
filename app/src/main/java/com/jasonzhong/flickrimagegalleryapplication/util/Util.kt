package com.jasonzhong.flickrimagegalleryapplication.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.v4.content.ContextCompat.getSystemService
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import com.jasonzhong.flickrimagegalleryapplication.R

object Util {

    fun getScreenWidthPixels(context: Activity): Int {
        val display = context.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    fun showErrorDialog(context: Context, buttonText: String?, title: String?, errorMessage: String?) {
        var errorMessage = errorMessage

        val alertDialogBuilder = AlertDialog.Builder(
            context
        )

        // set title
        alertDialogBuilder.setTitle(title)

        // set dialog message
        alertDialogBuilder
            .setMessage(errorMessage)
            .setCancelable(false)
            .setPositiveButton(buttonText) { dialog, id -> dialog.dismiss() }
        // create alert dialog
        val alertDialog = alertDialogBuilder.create()

        // show it
        alertDialog.show()
    }

    fun hideSoftKeyboard(context: Activity) {
        if (context.currentFocus != null) {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(context.currentFocus!!.windowToken, 0)
        }
    }

}

