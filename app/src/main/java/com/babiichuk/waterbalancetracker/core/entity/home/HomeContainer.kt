package com.babiichuk.waterbalancetracker.core.entity.home

import com.babiichuk.waterbalancetracker.core.utils.StateHolder

data class HomeContainer(
    val id: Int,
    val listOfChild: List<StateHolder<*>>,
) {

    companion object Factory {

        const val CUPS_CONTAINER_ID: Int = 11111
        const val AMOUNT_CONTAINER_ID: Int = 11112

        fun createDefaultCups(child: List<StateHolder<DefaultCup>>): HomeContainer {
            return HomeContainer(
                id = CUPS_CONTAINER_ID,
                listOfChild = child
            )
        }

        fun createDefaultAmount(child: List<StateHolder<DefaultAmount>>): HomeContainer {
            return HomeContainer(
                id = AMOUNT_CONTAINER_ID,
                listOfChild = child
            )
        }
    }
}
