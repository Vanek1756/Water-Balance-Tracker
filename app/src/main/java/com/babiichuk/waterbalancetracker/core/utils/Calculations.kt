package com.babiichuk.waterbalancetracker.core.utils

import com.babiichuk.waterbalancetracker.app.ui.utils.GenderType

fun calculateDailyWaterIntake(gender: GenderType, age: Int, weight: Int): Double {
    // Основна потреба у воді на основі ваги (мл/кг)
    val baseWaterIntakePerKg: Double = if (gender == GenderType.MAN) 35.0 else 31.0

    // Визначення вікового коефіцієнта
    val ageFactor: Double = when {
        age <= 30 -> 1.0
        age in 31..55 -> 0.95
        else -> 0.85
    }

    // Основна потреба у воді
    val baseWaterIntake: Double = weight * baseWaterIntakePerKg

    // Коригована потреба у воді
    val adjustedWaterIntake: Double = baseWaterIntake * ageFactor

    return adjustedWaterIntake
}