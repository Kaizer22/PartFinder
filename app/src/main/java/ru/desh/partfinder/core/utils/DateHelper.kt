package ru.desh.partfinder.core.utils

import java.text.SimpleDateFormat
import java.util.*

class DateHelper {
    companion object {
        private const val DAY_MS = 86400000L

        private val lastDayFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        private val lastWeekFormatter = SimpleDateFormat("EEE", Locale.getDefault())
        private val lastYearFormatter = SimpleDateFormat("MMM dd", Locale.getDefault())
        private val distantPastFormatter = SimpleDateFormat(" MMM dd, yyyy", Locale.getDefault())

        fun dateToText(date: Date): String {
            val currentDate = Date()
            val diff = currentDate.time - date.time
            val days = diff / 1000 / 60 / 60 / 24
            return when  {
                days == 0L -> lastDayFormatter.format(date.time)
                days < 7 -> lastWeekFormatter.format(date.time)
                days < 365 -> lastYearFormatter.format(date.time)
                else -> distantPastFormatter.format(date.time)
            }
        }
    }
}