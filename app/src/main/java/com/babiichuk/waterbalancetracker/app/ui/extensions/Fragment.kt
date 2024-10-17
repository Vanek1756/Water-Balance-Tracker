package com.babiichuk.waterbalancetracker.app.ui.extensions

import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.launchOnLifecycle(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) = lifecycleScope.launch { lifecycle.repeatOnLifecycle(state, block) }

fun Fragment.showToast(message: String, duration: Int) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.getColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(requireContext(), color)
}

fun Fragment.getDrawable(@DrawableRes id: Int): Drawable? {
    return AppCompatResources.getDrawable(requireContext(), id)
}
fun Fragment.toPx(dp: Int) = requireContext().toPx(dp)

fun Fragment.toPx(dp: Float) = requireContext().toPx(dp)