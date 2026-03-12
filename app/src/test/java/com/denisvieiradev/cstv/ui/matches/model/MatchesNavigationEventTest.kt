package com.denisvieiradev.cstv.ui.matches.model

import com.denisvieiradev.cstv.utils.fakeMatch
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class MatchesNavigationEventTest {

    class OpenMatchDetail {

        @Test
        fun `carries the correct Match`() {
            val match = fakeMatch(id = 7)
            val event = MatchesNavigationEvent.OpenMatchDetail(match)
            assertThat(event.match).isEqualTo(match)
        }
    }

    class NavigateToTokenScreen {

        @Test
        fun `is a singleton`() {
            assertThat(MatchesNavigationEvent.NavigateToTokenScreen)
                .isSameInstanceAs(MatchesNavigationEvent.NavigateToTokenScreen)
        }
    }

    class RecreateActivity {

        @Test
        fun `is a singleton`() {
            assertThat(MatchesNavigationEvent.RecreateActivity)
                .isSameInstanceAs(MatchesNavigationEvent.RecreateActivity)
        }
    }
}
