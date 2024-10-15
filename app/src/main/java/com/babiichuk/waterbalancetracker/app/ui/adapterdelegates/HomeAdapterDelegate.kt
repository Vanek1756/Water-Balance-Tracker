package com.babiichuk.waterbalancetracker.app.ui.adapterdelegates

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.extensions.getDefaultCupIconById
import com.babiichuk.waterbalancetracker.app.ui.extensions.getDefaultCupNameById
import com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates.AsyncListDiffDelegationAdapter
import com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates.diffAdapterDelegateLayoutContainer
import com.babiichuk.waterbalancetracker.core.entity.home.DefaultAmount
import com.babiichuk.waterbalancetracker.core.entity.home.DefaultCup
import com.babiichuk.waterbalancetracker.core.entity.home.HomeContainer
import com.babiichuk.waterbalancetracker.core.utils.State
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.getStateOrFalse
import com.babiichuk.waterbalancetracker.databinding.ItemAmountBinding
import com.babiichuk.waterbalancetracker.databinding.ItemCupBinding
import com.babiichuk.waterbalancetracker.databinding.ItemHomeContainerBinding

fun homeContainerAdapterDelegate(
    onCupClicked: (cupId: String) -> Unit,
    onAmountClicked: (amountId: Int) -> Unit
) = diffAdapterDelegateLayoutContainer<StateHolder<HomeContainer>, Any>(
    layout = R.layout.item_home_container,
    on = { item, _, _ -> item is StateHolder<*> && item.value is HomeContainer },
    same = { oldItem, newItem -> oldItem.value.id == newItem.value.id },
    contentEquals = { oldItem, newItem -> oldItem == newItem },
    changePayload = { _, _ ->
        mutableSetOf<String>().apply {
            add("ITEMS_CHANGED")
        }.takeIf { it.isNotEmpty() }
    }
) {

    val childLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    val childGridLayoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)

    val adapter = AsyncListDiffDelegationAdapter(
        cupsAdapterDelegate(onCupClicked),
        amountsAdapterDelegate(onAmountClicked)
    )

    val binding = ItemHomeContainerBinding.bind(itemView).apply {
    }

    bind {
        binding.apply {
            val isCupsContainer = item.value.id == HomeContainer.CUPS_CONTAINER_ID

            val nameResId =
                if (isCupsContainer) R.string.home_chooseDrink
                else R.string.home_selectAmount
            tvTitle.text = getString(nameResId)

            val childLayoutManager = if (isCupsContainer) childLinearLayoutManager else childGridLayoutManager
            rvCups.apply {
                layoutManager = childLayoutManager
                this.adapter = adapter
            }

            adapter.items = item.value.listOfChild
        }
    }
}

fun cupsAdapterDelegate(
    onCupClicked: (cupId: String) -> Unit
) = diffAdapterDelegateLayoutContainer<StateHolder<DefaultCup>, Any>(
    layout = R.layout.item_cup,
    on = { item, _, _ -> item is StateHolder<*> && item.value is DefaultCup },
    same = { oldItem, newItem -> oldItem.value.id == newItem.value.id },
    contentEquals = { oldItem, newItem -> oldItem == newItem },
    changePayload = { _, _ ->
        mutableSetOf<String>().apply {
            add("ITEMS_CHANGED")
        }.takeIf { it.isNotEmpty() }
    }
) {

    val binding = ItemCupBinding.bind(itemView).apply {
        root.setOnClickListener { onCupClicked.invoke(item.value.id) }
    }

    bind {
        binding.apply {
            val iconResId = item.value.id.getDefaultCupIconById()
            val nameResId = item.value.id.getDefaultCupNameById()
            ivIconCup.setImageResource(iconResId)
            tvVolume.text = getString(nameResId)

            root.isSelected = item.getStateOrFalse(State.SELECTED)
        }
    }
}

fun amountsAdapterDelegate(
    onItemClicked: (amountId: Int) -> Unit
) = diffAdapterDelegateLayoutContainer<StateHolder<DefaultAmount>, Any>(
    layout = R.layout.item_amount,
    on = { item, _, _ -> item is StateHolder<*> && item.value is DefaultAmount },
    same = { oldItem, newItem -> oldItem.value.id == newItem.value.id },
    contentEquals = { oldItem, newItem -> oldItem == newItem },
    changePayload = { _, _ ->
        mutableSetOf<String>().apply {
            add("ITEMS_CHANGED")
        }.takeIf { it.isNotEmpty() }
    }
) {
    val binding = ItemAmountBinding.bind(itemView).apply {
        root.setOnClickListener { onItemClicked.invoke(item.value.id) }
    }

    bind {
        binding.apply {
            val name =
                if (item.value.amount == 0) getString(R.string.home_other)
                else getString(R.string.home_current_rate, item.value.amount)

            tvAmount.text = name

            root.isSelected = item.getStateOrFalse(State.SELECTED)
        }
    }
}