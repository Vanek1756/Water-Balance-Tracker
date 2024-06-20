package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.storage.repository.CupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CupLoader @Inject constructor(
    private val cupRepository: CupRepository
) {

    private val scopeIo = CoroutineScope(Dispatchers.IO)

}