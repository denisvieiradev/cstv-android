package com.denisvieiradev.cstv.ui.matchdetail.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class MatchDetailNavigationEventTest {

    class NavigateBack {

        @Test
        fun `is a singleton`() {
            assertThat(MatchDetailNavigationEvent.NavigateBack)
                .isSameInstanceAs(MatchDetailNavigationEvent.NavigateBack)
        }
    }

    class NavigateToTokenScreen {

        @Test
        fun `is a singleton`() {
            assertThat(MatchDetailNavigationEvent.NavigateToTokenScreen)
                .isSameInstanceAs(MatchDetailNavigationEvent.NavigateToTokenScreen)
        }
    }
}
