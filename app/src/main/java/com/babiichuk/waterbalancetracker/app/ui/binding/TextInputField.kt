package com.babiichuk.waterbalancetracker.app.ui.binding

import android.text.Spanned
import android.widget.EditText
import com.babiichuk.waterbalancetracker.app.ui.utils.SimpleTextWatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

@JvmName("bindTextInputField-String")
suspend fun MutableStateFlow<String?>.bind(filed: EditText) {
    val watcher = SimpleTextWatcher(afterTextChanged = {
        val newValue =  getTextString(filed)
        if (newValue != value) {
            value = newValue?.toString()
        }
    })
    filed.addTextChangedListener(watcher)
    onEach { setText(filed, it) }
        .onCompletion { filed.removeTextChangedListener(watcher) }
        .collect()
}

@JvmName("bindTextInputField-Double")
suspend fun MutableStateFlow<Int>.bind(filed: EditText) {
    val watcher = SimpleTextWatcher(afterTextChanged = {
        try {
            val newValue =  getTextString(filed).toString().toInt()
            if (newValue != value) {
                value = newValue
            }
        } catch (e: Exception){
            e.stackTrace
        }
    })
    filed.addTextChangedListener(watcher)
    onEach { setText(filed, it.toString()) }
        .onCompletion { filed.removeTextChangedListener(watcher) }
        .collect()
}


private fun setText(view: EditText, text: CharSequence?) {
    val oldText = view.text
    if (text === oldText || text == null && oldText?.length == 0) {
        return
    }
    if (text is Spanned) {
        if (text == oldText) {
            return  // No change in the spans, so don't set anything.
        }
    } else if (!haveContentsChanged(text, oldText)) {
        return  // No content changes, so don't set anything.
    }
    view.setText(text)// Little bit changed from original
}

private fun getTextString(view: EditText): CharSequence? {
    return view.text
}

private fun haveContentsChanged(str1: CharSequence?, str2: CharSequence?): Boolean {
    if (str1 == null != (str2 == null)) {
        return true
    } else if (str1 == null) {
        return false
    }
    val length = str1.length
    if (length != str2!!.length) {
        return true
    }
    for (i in 0 until length) {
        if (str1[i] != str2[i]) {
            return true
        }
    }
    return false
}