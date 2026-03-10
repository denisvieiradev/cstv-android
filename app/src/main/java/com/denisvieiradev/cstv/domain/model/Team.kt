package com.denisvieiradev.cstv.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Team(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val players: List<Player> = emptyList()
) : Parcelable
