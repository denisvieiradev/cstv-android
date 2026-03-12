package com.denisvieiradev.cstv.ui.navigation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RouteTest {

    @Test
    fun `MATCHES route equals matches`() {
        assertThat(Route.MATCHES).isEqualTo("matches")
    }

    @Test
    fun `MATCH_DETAIL route equals match_detail`() {
        assertThat(Route.MATCH_DETAIL).isEqualTo("match_detail")
    }
}
