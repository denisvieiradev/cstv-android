package com.denisvieiradev.cstv.data.dto

import com.google.gson.annotations.SerializedName

data class PlayerDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null
)
