package ru.desh.partfinder.core.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.util.*

class DateHelperTest {
    companion object {
        // 10.01.2023 12:00
        private const val FIXED_TIMESTAMP_MS = 1673341200000

        private const val _2_DAYS = DateHelper.DAY_MS * 2
        private const val _128_DAYS = DateHelper.DAY_MS * 128
        private const val _370_DAYS = DateHelper.DAY_MS * 370
    }

    @Before
    fun before() {
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIMESTAMP_MS)
    }

    @After
    fun after() {
        DateTimeUtils.setCurrentMillisSystem()
    }

    @Test
    fun `should return valid date string`() {
        val duringLastDayExample = DateTime.now().toDate()
        val duringLastWeekExample = Date(duringLastDayExample.time - _2_DAYS)
        val duringLastYearExample = Date(duringLastDayExample.time - _128_DAYS)
        val distantPastExample = Date(duringLastDayExample.time - _370_DAYS)

        val lastDayString = DateHelper.dateToText(duringLastDayExample, Locale("ru"))
        Assertions.assertEquals("12:00 PM", lastDayString)

        val lastWeekString = DateHelper.dateToText(duringLastWeekExample, Locale("ru"))
        Assertions.assertEquals("вс", lastWeekString)

        val lastYearString = DateHelper.dateToText(duringLastYearExample, Locale("ru"))
        Assertions.assertEquals("сент. 04", lastYearString)

        val distantPastString = DateHelper.dateToText(distantPastExample, Locale("ru"))
        Assertions.assertEquals("янв. 05, 2022", distantPastString)
    }
}