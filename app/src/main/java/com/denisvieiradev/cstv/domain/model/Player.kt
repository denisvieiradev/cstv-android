package com.denisvieiradev.cstv.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val firstName: String?,
    val lastName: String?
) : Parcelable
