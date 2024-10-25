package com.babiichuk.waterbalancetracker.app.application.di

import android.content.Context
import com.babiichuk.waterbalancetracker.storage.dao.BeveragesDao
import com.babiichuk.waterbalancetracker.storage.dao.IntervalDao
import com.babiichuk.waterbalancetracker.storage.dao.UserDao
import com.babiichuk.waterbalancetracker.storage.dao.WaterRatePreferencesDao
import com.babiichuk.waterbalancetracker.storage.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideBeveragesDao(database: AppDatabase): BeveragesDao {
        return database.beveragesDao()
    }

    @Provides
    fun provideIntervalDao(database: AppDatabase): IntervalDao {
        return database.intervalDao()
    }

    @Provides
    fun providePreferencesWaterDao(database: AppDatabase): WaterRatePreferencesDao {
        return database.getWaterDao()
    }

}