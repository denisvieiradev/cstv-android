package com.denisvieiradev.network.data.remote.utils

class ApiException(val code: Int, override val message: String?) : Exception(message)
