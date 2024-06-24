package com.babiichuk.waterbalancetracker.app.ui.adapterdelegates

import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates.diffAdapterDelegateLayoutContainer
import com.babiichuk.waterbalancetracker.core.entity.AddNewFooter
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.databinding.ItemAddNewFooterBinding
import com.babiichuk.waterbalancetracker.databinding.ItemBeveragesBinding
import com.babiichuk.waterbalancetracker.storage.entity.BeveragesEntity

fun beveragesAdapterDelegate(
    onItemClicked: (beveragesId: Int) -> Unit
) = diffAdapterDelegateLayoutContainer<StateHolder<BeveragesEntity>, Any>(
    layout = R.layout.item_beverages,
    on = { item, _, _ -> item is StateHolder<*> && item.value is BeveragesEntity },
    same = { oldItem, newItem -> oldItem.value.id == newItem.value.id },
    contentEquals = { oldItem, newItem -> oldItem == newItem },
    changePayload = { _, _ ->
        mutableSetOf<String>().apply {
            add("ITEMS_CHANGED")
        }.takeIf { it.isNotEmpty() }
    }
) {
    val binding = ItemBeveragesBinding.bind(itemView).apply {
        cardView.setOnClickListener { onItemClicked.invoke(item.value.id) }
    }

    bind {
        binding.apply {
            val name = item.value.type.ifEmpty { getString(item.value.nameResId) }
            tvName.text = name
            tvVolume.text = getString(R.string.text_volume_ml, item.value.volume)
            ivIcon.setImageResource(item.value.iconId)
        }
    }
}

fun addNewFooterAdapterDelegate(
    onItemClicked: () -> Unit
) = diffAdapterDelegateLayoutContainer<StateHolder<AddNewFooter>, Any>(
    layout = R.layout.item_add_new_footer,
    on = { item, _, _ -> item is StateHolder<*> && item.value is AddNewFooter },
    same = { oldItem, newItem -> oldItem.value.id == newItem.value.id },
    contentEquals = { oldItem, newItem -> oldItem == newItem },
    changePayload = { _, _ ->
        mutableSetOf<String>().apply {
            add("ITEMS_CHANGED")
        }.takeIf { it.isNotEmpty() }
    }
) {
    val binding = ItemAddNewFooterBinding.bind(itemView).apply {
        cardView.setOnClickListener { onItemClicked.invoke() }
    }

    bind {
        binding.apply {

        }
    }
}