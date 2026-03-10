package com.denisvieiradev.cstv.data.session

class DemoSessionManager {

    private var remainingRequests = DEMO_REQUEST_LIMIT

    var isActive = false
        private set

    fun startDemo() {
        isActive = true
        remainingRequests = DEMO_REQUEST_LIMIT
    }

    fun reset() {
        isActive = false
        remainingRequests = DEMO_REQUEST_LIMIT
    }

    fun tryConsume(count: Int): Boolean {
        if (!isActive) return true
        if (remainingRequests <= 0) return false
        remainingRequests = (remainingRequests - count).coerceAtLeast(0)
        return true
    }

    companion object {
        const val DEMO_REQUEST_LIMIT = 6
    }
}
