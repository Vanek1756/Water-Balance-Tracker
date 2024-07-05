package com.babiichuk.waterbalancetracker.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.babiichuk.waterbalancetracker.R


@Entity(tableName = "interval_table")
data class IntervalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nameString: String = "",
    val nameResId: Int = 0,
    val intervalFrom: String,
    val intervalTo: String,
) {
    companion object Factory {

        fun create(
            id: Int,
            nameString: String,
            intervalFrom: String,
            intervalTo: String
        ): IntervalEntity {
            return IntervalEntity(
                id = id,
                nameString = nameString,
                intervalFrom = intervalFrom,
                intervalTo = intervalTo
            )
        }

        fun create(defaultInterval: DefaultTimeIntervalType): IntervalEntity {
            return IntervalEntity(
                nameResId = defaultInterval.nameResId,
                intervalFrom = defaultInterval.intervalFrom,
                intervalTo = defaultInterval.intervalTo
            )
        }

        fun createDefaultListOfIntervals(): List<IntervalEntity>{
           return DefaultTimeIntervalType.entries.map { IntervalEntity.create(it) }
        }
    }
}

enum class DefaultTimeIntervalType(
    val nameResId: Int,
    val intervalFrom: String,
    val intervalTo: String
) {
    BEFORE_BREAKFAST(R.string.interval_breakfast, "7:00", "9:40"),
    BEFORE_LUNCH(R.string.interval_lunch, "10:00", "14:00"),
    BEFORE_DINNER(R.string.interval_dinner, "14:20", "18:40"),
}