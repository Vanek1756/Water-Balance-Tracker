package com.babiichuk.waterbalancetracker.app.ui.adapterdelegates

import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates.diffAdapterDelegateLayoutContainer
import com.babiichuk.waterbalancetracker.core.entity.stats.StatsMonth
import com.babiichuk.waterbalancetracker.core.utils.State
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.getStateOrFalse
import com.babiichuk.waterbalancetracker.core.utils.setOnSingleClickListener
import com.babiichuk.waterbalancetracker.databinding.ItemMonthBinding

fun statisticAdapterDelegate(
    onMonthClicked: (monthId: Int) -> Unit
) = diffAdapterDelegateLayoutContainer<StateHolder<StatsMonth>, Any>(
    layout = R.layout.item_month,
    on = { item, _, _ -> item is StateHolder<*> && item.value is StatsMonth },
    same = { oldItem, newItem -> oldItem.value.id == newItem.value.id },
    contentEquals = { oldItem, newItem -> oldItem == newItem },
    changePayload = { _, _ ->
        mutableSetOf<String>().apply {
            add("ITEMS_CHANGED")
        }.takeIf { it.isNotEmpty() }
    }
) {

    val binding = ItemMonthBinding.bind(itemView).apply {
        root.setOnSingleClickListener { onMonthClicked.invoke(item.value.id) }
    }

    bind {
        binding.apply {
            tvVolume.text = item.value.name

            root.isSelected = item.getStateOrFalse(State.SELECTED)
        }
    }
}