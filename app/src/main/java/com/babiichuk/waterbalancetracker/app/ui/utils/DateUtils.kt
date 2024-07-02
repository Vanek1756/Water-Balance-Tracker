package com.babiichuk.waterbalancetracker.app.ui.utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun getTodayDate(): String {
    val dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
    return DateTime.now().toString(dateTimeFormatter)
}
