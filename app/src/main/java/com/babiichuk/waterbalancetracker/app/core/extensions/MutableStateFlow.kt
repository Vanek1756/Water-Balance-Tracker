package com.babiichuk.waterbalancetracker.app.core.extensions

import kotlinx.coroutines.flow.MutableStateFlow

inline fun MutableStateFlow<Boolean>?.withProgress(block: () -> Unit) {
    this?.compareAndSet(expect = false, update = true)
    block.invoke()
    this?.compareAndSet(expect = true, update = false)
}