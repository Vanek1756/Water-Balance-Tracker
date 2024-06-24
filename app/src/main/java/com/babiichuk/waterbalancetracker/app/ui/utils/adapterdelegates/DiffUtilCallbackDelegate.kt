package com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates

import androidx.recyclerview.widget.DiffUtil

abstract class DiffUtilCallbackDelegate<T>: DiffUtil.ItemCallback<T>() {

    abstract fun isForViewType(data: T): Boolean

}