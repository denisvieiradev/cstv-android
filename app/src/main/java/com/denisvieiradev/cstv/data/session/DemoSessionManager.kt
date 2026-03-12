package com.denisvieiradev.cstv.data.session

import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

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
        var consumed = false
        _remainingViews.update { current ->
            if (current <= 0) {
                consumed = false
                current
            } else {
                consumed = true
                current - 1
            }
        }
        return consumed
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
