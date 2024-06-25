package com.babiichuk.waterbalancetracker.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "cup_table")
data class CupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val beveragesId: Int,
    val nameString: String,
    val nameResId: Int,
    val iconId: Int,
    val volume: Int,
    val dateTime: String
){
    companion object Factory {

        fun create(beverages: BeveragesEntity): CupEntity {
            return CupEntity(
                beveragesId = beverages.id,
                nameString = beverages.nameString,
                nameResId = beverages.nameResId,
                iconId = beverages.iconId,
                volume = beverages.volume,
                dateTime = DateTime.now().toString()
            )
        }
    }
}
