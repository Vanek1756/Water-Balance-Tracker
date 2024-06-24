package com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallbackDelegatesManager<T>() : DiffUtilCallbackDelegate<T>() {

    val delegates = mutableListOf<DiffUtilCallbackDelegate<T>>()


    override fun isForViewType(data: T): Boolean {
        return delegates.firstOrNull { it.isForViewType(data) } != null
    }

    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return onDelegatesEquals(oldItem, newItem, false) {
            areItemsTheSame(oldItem, newItem)
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return onDelegatesEquals(oldItem, newItem, false) {
            areContentsTheSame(oldItem, newItem)
        }
    }

    override fun getChangePayload(oldItem: T & Any, newItem: T & Any): Any? {
        return onDelegatesEquals(oldItem, newItem, null) {
            getChangePayload(oldItem, newItem)
        }
    }

    private fun <R> onDelegatesEquals(
        oldItem: T,
        newItem: T,
        defValue: R,
        block: DiffUtil.ItemCallback<T>.() -> R
    ): R {
        val oldItemCallback = delegates.firstOrNull { it.isForViewType(oldItem) }
        val newItemCallback = delegates.firstOrNull { it.isForViewType(newItem) }

        return if (oldItemCallback == newItemCallback && oldItemCallback != null) {
            block(oldItemCallback)
        } else {
            defValue
        }
    }

}