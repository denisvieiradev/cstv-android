package com.denisvieiradev.cstv.domain.model

data class Team(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val players: List<Player> = emptyList()
)
