package com.denisvieiradev.cstv.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MatchStatus : Parcelable { RUNNING, NOT_STARTED, FINISHED, CANCELED, POSTPONED }
