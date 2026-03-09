package com.denisvieiradev.cstv.ui.matches.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MatchDateFormatterTest {

    private val ptBr = Locale("pt", "BR")
    private val utcZone = ZoneOffset.UTC
    private val today = LocalDate.of(2025, 4, 15)

    private fun utcString(date: LocalDate, hour: Int = 18, minute: Int = 0): String {
        return ZonedDateTime.of(date.atTime(hour, minute), ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    @Test
    fun `format returns empty string when scheduledAt is null`() {
        val result = MatchDateFormatter.format(null, today, ptBr, utcZone)
        assertEquals("", result)
    }

    @Test
    fun `format returns empty string when scheduledAt is invalid`() {
        val result = MatchDateFormatter.format("not-a-date", today, ptBr, utcZone)
        assertEquals("", result)
    }

    @Test
    fun `format returns Hoje when match is today`() {
        val scheduledAt = utcString(today)
        val result = MatchDateFormatter.format(scheduledAt, today, ptBr, utcZone)
        assertEquals("Hoje, 18:00", result)
    }

    @Test
    fun `format returns Amanha when match is tomorrow`() {
        val tomorrow = today.plusDays(1)
        val scheduledAt = utcString(tomorrow, 21, 30)
        val result = MatchDateFormatter.format(scheduledAt, today, ptBr, utcZone)
        assertEquals("Amanhã, 21:30", result)
    }

    @Test
    fun `format returns day abbreviation when match is within 2 days`() {
        val inTwoDays = today.plusDays(2)
        val scheduledAt = utcString(inTwoDays, 15, 0)
        val result = MatchDateFormatter.format(scheduledAt, today, ptBr, utcZone)
        assert(result.endsWith(", 15:00")) { "Expected day abbreviation format but got: $result" }
    }

    @Test
    fun `format returns day abbreviation for day 6`() {
        val inSixDays = today.plusDays(6)
        val scheduledAt = utcString(inSixDays, 20, 0)
        val result = MatchDateFormatter.format(scheduledAt, today, ptBr, utcZone)
        assert(result.endsWith(", 20:00")) { "Expected day abbreviation format but got: $result" }
    }

    @Test
    fun `format returns DDMM HHmm when match is 7 or more days away`() {
        val inSevenDays = today.plusDays(7)
        val scheduledAt = utcString(inSevenDays, 14, 0)
        val result = MatchDateFormatter.format(scheduledAt, today, ptBr, utcZone)
        assertEquals("22.04 14:00", result)
    }

    @Test
    fun `format returns DDMM HHmm for distant future`() {
        val farFuture = today.plusDays(30)
        val scheduledAt = utcString(farFuture, 10, 45)
        val result = MatchDateFormatter.format(scheduledAt, today, ptBr, utcZone)
        assertEquals("15.05 10:45", result)
    }

    @Test
    fun `format pads hours and minutes correctly`() {
        val scheduledAt = utcString(today, 9, 5)
        val result = MatchDateFormatter.format(scheduledAt, today, ptBr, utcZone)
        assertEquals("Hoje, 09:05", result)
    }
}
