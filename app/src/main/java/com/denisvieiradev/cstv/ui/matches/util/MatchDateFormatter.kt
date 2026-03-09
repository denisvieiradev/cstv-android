package com.denisvieiradev.cstv.ui.matches.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object MatchDateFormatter {

    fun format(
        scheduledAt: String?,
        now: LocalDate,
        locale: Locale = Locale("pt", "BR"),
        zoneId: ZoneId = ZoneId.systemDefault()
    ): String {
        scheduledAt ?: return ""
        val zonedDateTime = parseToLocalZone(scheduledAt, zoneId) ?: return ""
        val matchDate = zonedDateTime.toLocalDate()
        val days = ChronoUnit.DAYS.between(now, matchDate)
        val time = formatTime(zonedDateTime)
        return when {
            days == 0L -> "Hoje, $time"
            days == 1L -> "Amanhã, $time"
            days in 2L..6L -> "${formatDayAbbrev(zonedDateTime, locale)}, $time"
            else -> "${formatDayMonth(zonedDateTime)} $time"
        }
    }

    private fun parseToLocalZone(scheduledAt: String, zoneId: ZoneId): ZonedDateTime? {
        return runCatching {
            ZonedDateTime.parse(scheduledAt).withZoneSameInstant(zoneId)
        }.getOrNull()
    }

    private fun formatTime(dateTime: ZonedDateTime): String {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    private fun formatDayAbbrev(dateTime: ZonedDateTime, locale: Locale): String {
        val full = dateTime.format(DateTimeFormatter.ofPattern("EEE", locale))
        return full.replaceFirstChar { it.uppercase(locale) }.trimEnd('.')
    }

    private fun formatDayMonth(dateTime: ZonedDateTime): String {
        return dateTime.format(DateTimeFormatter.ofPattern("dd.MM"))
    }
}
