package com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateLayoutContainerViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer

inline fun <reified I : T, T> diffAdapterDelegateLayoutContainer(
    @LayoutRes layout: Int,
    noinline on: (item: T, items: List<T>, position: Int) -> Boolean = { item, _, _ -> item is I },
    noinline same: (oldItem: I, newItem: I) -> Boolean = { oldItem, newItem -> oldItem == newItem },
    noinline contentEquals: (oldItem: I, newItem: I) -> Boolean = { oldItem, newItem -> oldItem.hashCode() == newItem.hashCode() },
    noinline changePayload: (oldItem: I, newItem: I) -> Any? = { _, _ -> null },
    noinline layoutInflater: (parent: ViewGroup, layoutRes: Int) -> View = { parent, layoutRes ->
        LayoutInflater.from(parent.context).inflate(
            layoutRes,
            parent,
            false
        )
    },
    noinline block: AdapterDelegateLayoutContainerViewHolder<I>.() -> Unit
) = DiffListItemAdapterDelegate<T>(
    DslDiffUtilCallbackDelegate(on, same, contentEquals, changePayload),
    adapterDelegateLayoutContainer<I, T>(layout, on, layoutInflater, block)
)

@Suppress("UNCHECKED_CAST")
class DslDiffUtilCallbackDelegate<I: T, T>(
    private val on: (item: T, items: List<T>, position: Int) -> Boolean,
    private val areSame: (oldItem: I, newItem: I) -> Boolean,
    private val areContentEquals: (oldItem: I, newItem: I) -> Boolean,
    private val changePayload: (oldItem: I, newItem: I) -> Any?

): DiffUtilCallbackDelegate<T>() {

    override fun isForViewType(data: T): Boolean {
        return on(data, listOf(data), 0)
    }

    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return areSame(oldItem as I, newItem as I)
    }

    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return areContentEquals(oldItem as I, newItem as I)
    }

    override fun getChangePayload(oldItem: T & Any, newItem: T & Any): Any? {
        return changePayload(oldItem as I, newItem as I)
    }
}