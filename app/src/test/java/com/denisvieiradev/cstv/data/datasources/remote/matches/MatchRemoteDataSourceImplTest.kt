package com.denisvieiradev.cstv.data.datasources.remote.matches

import com.denisvieiradev.cstv.utils.fakeMatchDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MatchRemoteDataSourceImplTest {

    private val mockApi: MatchApi = mockk()
    private val dataSource = MatchRemoteDataSourceImpl(mockApi)

    @Test
    fun `getRunningMatches should map API response to domain models`() = runTest {
        val dto = fakeMatchDto(id = 1, status = "running", teamAName = "Natus Vincere", teamBName = "FaZe Clan")
        coEvery { mockApi.getRunningMatches() } returns listOf(dto)

        val result = dataSource.getRunningMatches()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[0].teamA?.name).isEqualTo("Natus Vincere")
        assertThat(result[0].teamB?.name).isEqualTo("FaZe Clan")
    }

    @Test
    fun `getRunningMatches should return empty list when API returns empty`() = runTest {
        coEvery { mockApi.getRunningMatches() } returns emptyList()

        val result = dataSource.getRunningMatches()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getUpcomingMatches should map API response to domain models`() = runTest {
        val dto = fakeMatchDto(id = 2, status = "not_started")
        coEvery { mockApi.getUpcomingMatches(any()) } returns listOf(dto)

        val result = dataSource.getUpcomingMatches()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(2)
    }

    @Test
    fun `getUpcomingMatches should return empty list when API returns empty`() = runTest {
        coEvery { mockApi.getUpcomingMatches(any()) } returns emptyList()

        val result = dataSource.getUpcomingMatches()

        assertThat(result).isEmpty()
    }
}
