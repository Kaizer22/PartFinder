package ru.desh.partfinder.core.utils

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

class DateHelper {
    companion object {
        const val DAY_MS = 86400000L

        const val LAST_DAY_PATTERN = "hh:mm a"
        const val LAST_WEEK_PATTERN = "EEE"
        const val LAST_YEAR_PATTERN = "MMM dd"
        const val DISTANT_PAST_PATTERN = "MMM dd, yyyy"


        fun dateToText(date: Date, locale: Locale): String {
            val currentDate = DateTime.now().toDate()
            val diff = currentDate.time - date.time
            val days = diff / DAY_MS
            return when {
                days == 0L -> SimpleDateFormat(LAST_DAY_PATTERN, locale).format(date.time)
                days < 7 -> SimpleDateFormat(LAST_WEEK_PATTERN, locale).format(date.time)
                days < 365 -> SimpleDateFormat(LAST_YEAR_PATTERN, locale).format(date.time)
                else -> SimpleDateFormat(DISTANT_PAST_PATTERN, locale).format(date.time)
            }
        }
    }
}