package com.babiichuk.waterbalancetracker.storage.repository

import com.babiichuk.waterbalancetracker.storage.dao.BeveragesDao
import com.babiichuk.waterbalancetracker.storage.entity.CupEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CupRepository @Inject constructor(private val beveragesDao: BeveragesDao) {

    fun getCupsFlow(userId: String): Flow<List<CupEntity>> {
        return beveragesDao.getAllCupsByUserIdFlow(userId)
    }

    suspend fun getCups(userId: String): List<CupEntity> {
        return beveragesDao.getAllCupsByUserId(userId)
    }

    suspend fun insertCup(cup: CupEntity) {
        beveragesDao.insertCup(cup)
    }

    suspend fun deleteCup(id: Int) {
        beveragesDao.deleteCupById(id)
    }

    suspend fun getCup(id: Int): CupEntity? {
        return beveragesDao.getCupById(id)
    }
}
