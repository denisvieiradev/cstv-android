package com.denisvieiradev.cstv.data.session

class DemoSessionManager {

    private var remainingViews = DEMO_DETAIL_LIMIT

    var isActive = false
        private set

    fun startDemo() {
        isActive = true
        remainingViews = DEMO_DETAIL_LIMIT
    }

    fun reset() {
        isActive = false
        remainingViews = DEMO_DETAIL_LIMIT
    }

    fun tryConsume(): Boolean {
        if (!isActive) return true
        if (remainingViews <= 0) return false
        remainingViews--
        return true
    }

    companion object {
        const val DEMO_DETAIL_LIMIT = 6
    }
}
