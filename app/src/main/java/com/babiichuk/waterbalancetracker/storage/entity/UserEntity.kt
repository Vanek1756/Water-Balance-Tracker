package com.babiichuk.waterbalancetracker.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.babiichuk.waterbalancetracker.app.ui.utils.GenderType

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val age: Int,
    val weight: Int,
    val gender: GenderType,
    val email: String,
    val recommendedWaterRate: Double,
    val currentWaterRate: Double
) {

    fun isNewUser(): Boolean{
        val ageEmpty = age == 0
        val weightEmpty = weight == 0
        val genderEmpty = gender == GenderType.UNKNOWN
        val waterRateEmpty = recommendedWaterRate == 0.0
        return ageEmpty || weightEmpty || genderEmpty || waterRateEmpty
    }
    companion object Factory {

        fun create(id: String, email: String?, name: String?): UserEntity {
            return UserEntity(
                id = id,
                name = name ?: "",
                age = 0,
                weight = 0,
                gender = GenderType.UNKNOWN,
                email = email ?: "",
                recommendedWaterRate = 0.0,
                currentWaterRate = 0.0
            )
        }
    }
}
