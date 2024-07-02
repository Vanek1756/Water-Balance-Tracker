package com.babiichuk.waterbalancetracker.core.entity

import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.mapToStateHolderList
import com.babiichuk.waterbalancetracker.storage.entity.CupEntity
import com.babiichuk.waterbalancetracker.storage.entity.IntervalEntity

data class IntervalContainer(
    val id: Int,
    val intervalEntity: IntervalEntity,
    val listOfChild: List<StateHolder<*>>,
) {

    companion object Factory {

        fun create(listOfCups: List<CupEntity>, intervalEntity: IntervalEntity): IntervalContainer {
            val child = mutableListOf<Any>()
            child.addAll(listOfCups)
            child.add(AddNewFooter(intervalEntity.id.toString()))
            return IntervalContainer(
                id = intervalEntity.id,
                intervalEntity = intervalEntity,
                listOfChild = child.mapToStateHolderList()
            )
        }
    }
}
