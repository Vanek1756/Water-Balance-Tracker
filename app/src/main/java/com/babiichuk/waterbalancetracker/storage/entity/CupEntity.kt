package com.babiichuk.waterbalancetracker.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.babiichuk.waterbalancetracker.app.ui.utils.parseToString
import java.time.LocalDate

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
                dateTime = parseToString(LocalDate.now())
            )
        }
    }
}
