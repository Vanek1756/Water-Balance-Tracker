package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.storage.repository.BeveragesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeveragesLoader @Inject constructor(
    private val beveragesRepository: BeveragesRepository
) {

    private val scopeIo = CoroutineScope(Dispatchers.IO)

}