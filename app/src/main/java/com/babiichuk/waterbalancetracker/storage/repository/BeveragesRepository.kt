package com.babiichuk.waterbalancetracker.storage.repository

import android.util.Log
import com.babiichuk.waterbalancetracker.storage.dao.BeveragesDao
import com.babiichuk.waterbalancetracker.storage.entity.BeveragesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BeveragesRepository @Inject constructor(private val beveragesDao: BeveragesDao) {

    suspend fun insertBeverage(beveragesEntity: BeveragesEntity) {
        Log.e("Database", "insert: $beveragesEntity")
        beveragesDao.insertBeverage(beveragesEntity)
    }

    suspend fun insertBeverages(beveragesList: List<BeveragesEntity>) {
        beveragesDao.insertBeverages(beveragesList)
    }

    fun getBeveragesFlow(): Flow<List<BeveragesEntity>> {
        return beveragesDao.getAllBeveragesFlow()
    }

    suspend fun getBeverages(): List<BeveragesEntity> {
        return beveragesDao.getAllBeverages()
    }

    suspend fun deleteBeverage(id: Int) {
        beveragesDao.deleteBeverageById(id)
    }

    suspend fun getBeverage(id: Int): BeveragesEntity? {
        return beveragesDao.getBeverageById(id)
    }

}
