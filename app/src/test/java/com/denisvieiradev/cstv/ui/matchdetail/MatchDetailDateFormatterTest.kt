package com.denisvieiradev.cstv.ui.matchdetail

import com.denisvieiradev.cstv.ui.matches.util.MatchDateFormatter
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MatchDetailDateFormatterTest {

    private val ptBr = Locale("pt", "BR")
    private val utcZone = ZoneOffset.UTC
    private val today = LocalDate.of(2025, 6, 10)

    private fun utcString(date: LocalDate, hour: Int = 15, minute: Int = 0): String {
        return ZonedDateTime.of(date.atTime(hour, minute), ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    @Test
    fun `scheduledAt with today date returns Hoje HH mm`() {
        val scheduledAt = utcString(today, 18, 30)

        val result = MatchDateFormatter.format(scheduledAt, today, ptBr, utcZone)

        assertThat(result).isEqualTo("Hoje, 18:30")
    }

    @Test
    fun `scheduledAt with future date returns localized format`() {
        val futureDate = today.plusDays(10)
        val scheduledAt = utcString(futureDate, 20, 0)

        val result = MatchDateFormatter.format(scheduledAt, today, ptBr, utcZone)

        assertThat(result).isEqualTo("20.06 20:00")
    }

    @Test
    fun `scheduledAt null returns empty string`() {
        val result = MatchDateFormatter.format(null, today, ptBr, utcZone)

        assertThat(result).isEmpty()
    }
}
