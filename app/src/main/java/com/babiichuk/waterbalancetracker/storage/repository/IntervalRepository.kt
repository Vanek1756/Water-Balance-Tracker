package com.babiichuk.waterbalancetracker.storage.repository

import android.util.Log
import com.babiichuk.waterbalancetracker.storage.dao.IntervalDao
import com.babiichuk.waterbalancetracker.storage.entity.IntervalEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IntervalRepository @Inject constructor(private val intervalsDao: IntervalDao) {

    suspend fun insertInterval(intervalsEntity: IntervalEntity) {
        Log.e("Database", "insert: $intervalsEntity")
        intervalsDao.insertInterval(intervalsEntity)
    }

    suspend fun insertIntervals(intervalsEntity: List<IntervalEntity>) {
        intervalsDao.insertIntervals(intervalsEntity)
    }

    fun getIntervalsFlow(): Flow<List<IntervalEntity>> {
        return intervalsDao.getAllIntervalsFlow()
    }

    suspend fun getIntervals(): List<IntervalEntity> {
        return intervalsDao.getAllIntervals()
    }

    suspend fun deleteInterval(id: Int) {
        intervalsDao.deleteIntervalById(id)
    }

    suspend fun getInterval(id: Int): IntervalEntity? {
        return intervalsDao.getIntervalById(id)
    }

}
