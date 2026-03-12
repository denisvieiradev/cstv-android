package com.denisvieiradev.cstv.data.session

import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import kotlinx.coroutines.flow.MutableStateFlow

class DemoSessionManager(
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val demoToken: String
) {

    private val _remainingViews = MutableStateFlow(DEMO_DETAIL_LIMIT)
    private val _isActive = MutableStateFlow(false)

    val isActive: Boolean get() = _isActive.value

    init { restoreIfNeeded() }

    fun startDemo() {
        sessionLocalDataSource.saveDemoUsed()
        _isActive.value = true
        _remainingViews.value = DEMO_DETAIL_LIMIT
    }

    fun reset() {
        _isActive.value = false
        _remainingViews.value = DEMO_DETAIL_LIMIT
    }

    fun tryConsume(): Boolean {
        if (!_isActive.value) return true
        val current = _remainingViews.value
        if (current <= 0) return false
        _remainingViews.value = current - 1
        return true
    }

    fun isDemoAlreadyUsed(): Boolean = sessionLocalDataSource.isDemoUsed()

    private fun restoreIfNeeded() {
        if (sessionLocalDataSource.isDemoUsed() && sessionLocalDataSource.getToken() == demoToken) {
            _isActive.value = true
            _remainingViews.value = 0
        }
    }

    companion object {
        const val DEMO_DETAIL_LIMIT = 6
    }
}
