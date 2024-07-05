package com.babiichuk.waterbalancetracker.app.ui.adapterdelegates

import androidx.recyclerview.widget.GridLayoutManager
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates.AsyncListDiffDelegationAdapter
import com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates.diffAdapterDelegateLayoutContainer
import com.babiichuk.waterbalancetracker.core.entity.AddNewFooter
import com.babiichuk.waterbalancetracker.core.entity.IntervalContainer
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.databinding.ItemAddCupBinding
import com.babiichuk.waterbalancetracker.databinding.ItemCupsBinding
import com.babiichuk.waterbalancetracker.databinding.ItemIntervalContainerBinding
import com.babiichuk.waterbalancetracker.storage.entity.CupEntity

fun cupsContainerAdapterDelegate(
    onAddButtonClicked: (containerId: Int) -> Unit,
    onCupClicked: (cupId: Int) -> Unit
) = diffAdapterDelegateLayoutContainer<StateHolder<IntervalContainer>, Any>(
    layout = R.layout.item_interval_container,
    on = { item, _, _ -> item is StateHolder<*> && item.value is IntervalContainer },
    same = { oldItem, newItem -> oldItem.value.id == newItem.value.id },
    contentEquals = { oldItem, newItem -> oldItem == newItem },
    changePayload = { _, _ ->
        mutableSetOf<String>().apply {
            add("ITEMS_CHANGED")
        }.takeIf { it.isNotEmpty() }
    }
) {

    val childLayoutManager =  GridLayoutManager(context, 5, GridLayoutManager.VERTICAL, false)

    val adapter = AsyncListDiffDelegationAdapter(
        cupsAdapterDelegate(onCupClicked),
        addCupAdapterDelegate {onAddButtonClicked.invoke(item.value.id)}
    )

    val binding = ItemIntervalContainerBinding.bind(itemView).apply {
    }

    bind {
        binding.apply {
            val formattedString = getString(
                R.string.interval_breakfast,
                item.value.intervalEntity.intervalFrom,
                item.value.intervalEntity.intervalTo
            )
            tvIntervalTitle.text = formattedString
            rvCups.apply {
                layoutManager = childLayoutManager
                this.adapter = adapter
            }
            adapter.items = item.value.listOfChild
        }
    }
}

fun cupsAdapterDelegate(
    onCupClicked: (cupId: Int) -> Unit
) = diffAdapterDelegateLayoutContainer<StateHolder<CupEntity>, Any>(
    layout = R.layout.item_cups,
    on = { item, _, _ -> item is StateHolder<*> && item.value is CupEntity },
    same = { oldItem, newItem -> oldItem.value.id == newItem.value.id },
    contentEquals = { oldItem, newItem -> oldItem == newItem },
    changePayload = { _, _ ->
        mutableSetOf<String>().apply {
            add("ITEMS_CHANGED")
        }.takeIf { it.isNotEmpty() }
    }
) {

    val binding = ItemCupsBinding.bind(itemView).apply {
        root.setOnClickListener { onCupClicked.invoke(item.value.id) }
    }

    bind {
        binding.apply {
            ivIconCup.setImageResource(item.value.iconId)
            tvVolume.text = getString(R.string.home_current_rate, item.value.volume)
        }
    }
}

fun addCupAdapterDelegate(
    onItemClicked: () -> Unit
) = diffAdapterDelegateLayoutContainer<StateHolder<AddNewFooter>, Any>(
    layout = R.layout.item_add_cup,
    on = { item, _, _ -> item is StateHolder<*> && item.value is AddNewFooter },
    same = { oldItem, newItem -> oldItem.value.id == newItem.value.id },
    contentEquals = { oldItem, newItem -> oldItem == newItem },
    changePayload = { _, _ ->
        mutableSetOf<String>().apply {
            add("ITEMS_CHANGED")
        }.takeIf { it.isNotEmpty() }
    }
) {
    val binding = ItemAddCupBinding.bind(itemView).apply {
        root.setOnClickListener { onItemClicked.invoke() }
    }

    bind {
        binding.apply {

        }
    }
}