package com.babiichuk.waterbalancetracker.core.entity.home

import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.mapToStateHolderList

data class DefaultCup(
    val id: String
) {
    companion object Factory {

        fun createDefaultCups(): List<StateHolder<DefaultCup>> {
            return DefaultCupEnum.entries.map { DefaultCup(it.id) }.mapToStateHolderList()
        }
    }
}

data class DefaultAmount(
    val id: Int,
    var amount: Int
) {
    companion object Factory {
        const val DEFAULT_AMOUNT_100: Int = 100
        const val DEFAULT_AMOUNT_200: Int = 200
        const val DEFAULT_AMOUNT_300: Int = 300
        const val DEFAULT_AMOUNT_ANOTHER: Int = 0
        val DEFAULT_AMOUNT_NOTHING: Int = -1

        val listOfAmount: List<Int> = listOf(
            DEFAULT_AMOUNT_100,
            DEFAULT_AMOUNT_200,
            DEFAULT_AMOUNT_300,
            DEFAULT_AMOUNT_ANOTHER
        )

        fun createDefaultAmount(): List<StateHolder<DefaultAmount>> {
           return listOfAmount.map { DefaultAmount(it, it) }.mapToStateHolderList()
        }
    }
}
