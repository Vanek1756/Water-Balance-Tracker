package com.babiichuk.waterbalancetracker.storage.repository

import com.babiichuk.waterbalancetracker.storage.dao.BeveragesDao
import com.babiichuk.waterbalancetracker.storage.entity.CupEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CupRepository @Inject constructor(private val beveragesDao: BeveragesDao) {

    fun getCupsFlow(): Flow<List<CupEntity>> {
        return beveragesDao.getAllCupsFlow()
    }

    fun getAllCupsForTodayFlow(today: String): Flow<List<CupEntity>> {
        return beveragesDao.getAllCupsForTodayFlow(today)
    }

    fun getAllCupsByPeriod(startDate: String, endDate: String): List<CupEntity> {
        return beveragesDao.getAllCupsByPeriod(startDate, endDate)
    }

    suspend fun getCups(): List<CupEntity> {
        return beveragesDao.getAllCups()
    }

    suspend fun insertCup(cup: CupEntity) {
        beveragesDao.insertCup(cup)
    }

    suspend fun insertCups(cups: List<CupEntity>) {
        beveragesDao.insertCups(cups)
    }

    suspend fun deleteCup(id: Int) {
        beveragesDao.deleteCupById(id)
    }

    suspend fun getCup(id: Int): CupEntity? {
        return beveragesDao.getCupById(id)
    }
}
