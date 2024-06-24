package com.babiichuk.waterbalancetracker.app.ui.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowInsetsCompat

@Suppress("unused")
fun Context.toPx(size: Int): Int = (size * Resources.getSystem().displayMetrics.density).toInt()

@Suppress("unused")
fun Context.toPx(size: Float): Float = size * Resources.getSystem().displayMetrics.density

@Suppress("unused")
fun Context.spToPx(size: Int): Int = (size * Resources.getSystem().displayMetrics.scaledDensity).toInt()

@Suppress("unused")
fun Context.spToPx(size: Float): Float = (size * Resources.getSystem().displayMetrics.scaledDensity).toFloat()

fun Context.hideKeyboard(view: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view.windowInsetsController?.hide(WindowInsetsCompat.Type.ime())
    } else {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}