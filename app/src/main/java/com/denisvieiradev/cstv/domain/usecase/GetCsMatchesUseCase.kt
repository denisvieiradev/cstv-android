package com.denisvieiradev.cstv.domain.usecase

import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.repository.MatchRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetCsMatchesUseCase(private val repository: MatchRepository) {
    suspend operator fun invoke(): List<Match> = coroutineScope {
        val running = async { repository.getRunningMatches() }
        val upcoming = async { repository.getUpcomingMatches() }
        running.await() + upcoming.await()
    }
}
