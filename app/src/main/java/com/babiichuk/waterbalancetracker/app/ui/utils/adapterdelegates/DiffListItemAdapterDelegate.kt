package com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

data class DiffListItemAdapterDelegate<I> (
//    val itemViewType: Int,
    val diffItemCallback: DiffUtilCallbackDelegate<I>,
    val delegate: AdapterDelegate<List<I>>
)
