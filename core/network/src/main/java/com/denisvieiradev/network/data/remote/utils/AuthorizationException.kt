package com.denisvieiradev.network.data.remote.utils

class AuthorizationException(code: Int) : Exception("HTTP $code: Unauthorized")
