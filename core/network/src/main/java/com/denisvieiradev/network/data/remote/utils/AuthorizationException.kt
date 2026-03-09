package com.denisvieiradev.network.data.remote.utils

import java.io.IOException

class AuthorizationException(code: Int) : IOException("HTTP $code: Unauthorized")
