package com.denisvieiradev.cstv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.ui.matchdetail.MatchDetailScreenRoot
import com.denisvieiradev.cstv.ui.matchdetail.MatchDetailViewModel
import com.denisvieiradev.cstv.ui.matches.MatchesScreenRoot

@Composable
fun MatchesNavHost(
    navController: NavHostController,
    onNavigateToTokenScreen: () -> Unit,
    onRecreateActivity: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Route.MATCHES
    ) {
        composable(Route.MATCHES) {
            MatchesScreenRoot(
                onNavigateToMatchDetail = { match ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(MatchDetailViewModel.EXTRA_MATCH, match)
                    navController.navigate(Route.MATCH_DETAIL)
                },
                onNavigateToTokenScreen = onNavigateToTokenScreen,
                onRecreateActivity = onRecreateActivity
            )
        }
        composable(Route.MATCH_DETAIL) {
            val match = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Match>(MatchDetailViewModel.EXTRA_MATCH)
            MatchDetailScreenRoot(
                match = match,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTokenScreen = onNavigateToTokenScreen
            )
        }
    }
}
