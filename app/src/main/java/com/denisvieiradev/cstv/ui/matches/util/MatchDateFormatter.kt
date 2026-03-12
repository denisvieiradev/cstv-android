package com.denisvieiradev.cstv.ui.matches.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object MatchDateFormatter {

    private const val FORMAT_TIME = "HH:mm"
    private const val FORMAT_DAY_ABBREV = "EEE"
    private const val FORMAT_DAY_MONTH = "dd.MM"
    private const val LANG_PT = "pt"

    private const val DAY_TODAY = 0L
    private const val DAY_TOMORROW = 1L
    private const val DAYS_WEEK_START = 2L
    private const val DAYS_WEEK_END = 6L

    private val TIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_TIME)
    private val DAY_MONTH_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DAY_MONTH)

    fun format(
        scheduledAt: String?,
        now: LocalDate,
        locale: Locale = Locale.getDefault(),
        zoneId: ZoneId = ZoneId.systemDefault(),
        todayLabel: String = if (locale.language == LANG_PT) "Hoje" else "Today",
        tomorrowLabel: String = if (locale.language == LANG_PT) "Amanhã" else "Tomorrow"
    ): String {
        scheduledAt ?: return ""
        val zonedDateTime = parseToLocalZone(scheduledAt, zoneId) ?: return ""
        val matchDate = zonedDateTime.toLocalDate()
        val days = ChronoUnit.DAYS.between(now, matchDate)
        val time = formatTime(zonedDateTime)
        return when {
            days == DAY_TODAY -> "$todayLabel, $time"
            days == DAY_TOMORROW -> "$tomorrowLabel, $time"
            days in DAYS_WEEK_START..DAYS_WEEK_END -> "${formatDayAbbrev(zonedDateTime, locale)}, $time"
            else -> "${formatDayMonth(zonedDateTime)} $time"
        }
    }

    private fun parseToLocalZone(scheduledAt: String, zoneId: ZoneId): ZonedDateTime? {
        return runCatching {
            ZonedDateTime.parse(scheduledAt).withZoneSameInstant(zoneId)
        }.getOrNull()
    }

    private fun formatTime(dateTime: ZonedDateTime): String {
        return dateTime.format(TIME_FORMATTER)
    }

    private fun formatDayAbbrev(dateTime: ZonedDateTime, locale: Locale): String {
        val full = dateTime.format(DateTimeFormatter.ofPattern(FORMAT_DAY_ABBREV, locale))
        return full.replaceFirstChar { it.uppercase(locale) }.trimEnd('.')
    }

    private fun formatDayMonth(dateTime: ZonedDateTime): String {
        return dateTime.format(DAY_MONTH_FORMATTER)
    }
}
