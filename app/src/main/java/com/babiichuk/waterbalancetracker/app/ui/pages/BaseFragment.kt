package com.babiichuk.waterbalancetracker.app.ui.pages

import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.babiichuk.waterbalancetracker.app.ui.extensions.showToast

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    fun showError(message: String?) {
        message?.let { showToast(message, Toast.LENGTH_SHORT) }
    }

    fun navigateTo(@IdRes resId: Int){
        findNavController().navigate(resId)
    }

    fun navigateTo(directions: NavDirections){
        findNavController().navigate(directions)
    }

    fun navigateUp(){
        findNavController().navigateUp()
    }
}