package com.denisvieiradev.cstv.data.session

import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource

class DemoSessionManager(
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val demoToken: String
) {

    private var remainingViews = DEMO_DETAIL_LIMIT

    var isActive = false
        private set

    init { restoreIfNeeded() }

    fun startDemo() {
        sessionLocalDataSource.saveDemoUsed()
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

    fun isDemoAlreadyUsed(): Boolean = sessionLocalDataSource.isDemoUsed()

    private fun restoreIfNeeded() {
        if (sessionLocalDataSource.isDemoUsed() && sessionLocalDataSource.getToken() == demoToken) {
            isActive = true
            remainingViews = 0
        }
    }

    companion object {
        const val DEMO_DETAIL_LIMIT = 6
    }
}
