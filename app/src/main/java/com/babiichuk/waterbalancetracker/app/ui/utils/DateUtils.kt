package com.babiichuk.waterbalancetracker.app.ui.utils

import com.babiichuk.waterbalancetracker.storage.entity.CupEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun getTodayDate(): String {
    return LocalDate.now().format(formatter)
}

fun parseToDate(cupEntity: CupEntity): LocalDate {
    return LocalDate.parse(cupEntity.dateTime, formatter)
}

fun parseToString(localDate: LocalDate): String {
    return localDate.format(formatter)
}
