package com.babiichuk.waterbalancetracker.app.ui.pages

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel : ViewModel() {

    val mutableIsProgressVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val mutableErrorMessage: MutableSharedFlow<String?> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val mutableErrorType: MutableSharedFlow<ErrorType?> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    fun onError(errorType: ErrorType) {
        mutableErrorType.tryEmit(errorType)
    }

    fun onError(msg: String) {
        mutableErrorMessage.tryEmit(msg)
    }

}

enum class ErrorType {
    NETWORK, // IO
    TIMEOUT, // Socket
    UNKNOWN, //Anything else
    SESSION_EXPIRED
}