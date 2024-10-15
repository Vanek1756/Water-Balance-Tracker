package com.babiichuk.waterbalancetracker.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "cup_table")
data class CupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cupId: String,
    val amount: Int,
    val dateTime: String
){
    companion object Factory {

        fun create(cupId: String, amount: Int): CupEntity {
            return CupEntity(
                cupId = cupId,
                amount = amount,
                dateTime = DateTime.now().toString()
            )
        }
    }
}
