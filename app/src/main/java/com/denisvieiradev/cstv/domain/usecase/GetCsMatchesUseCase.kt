package com.denisvieiradev.cstv.domain.usecase

import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.repository.MatchRepository

class GetCsMatchesUseCase(private val repository: MatchRepository) {
    suspend operator fun invoke(): List<Match> {
        val running = repository.getRunningMatches()
        val upcoming = repository.getUpcomingMatches()
        return running + upcoming
    }
}
