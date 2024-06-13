package com.babiichuk.waterbalancetracker.app.ui.extensions

import android.widget.Toast
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