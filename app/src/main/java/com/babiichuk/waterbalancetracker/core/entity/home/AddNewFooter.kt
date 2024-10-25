package com.babiichuk.waterbalancetracker.core.entity.home

import com.babiichuk.waterbalancetracker.core.utils.getRandomUuid

data class AddNewFooter(
    val id: String
) {

    companion object Factory {
        fun create(): AddNewFooter {
            return AddNewFooter(getRandomUuid())
        }
    }
}
