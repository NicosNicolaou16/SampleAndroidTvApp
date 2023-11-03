package com.nicos.sampleandroidtvapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nicos.sampleandroidtvapp.ui.launcher_screen.LauncherScreen
import com.nicos.sampleandroidtvapp.ui.ship_details_screen.SHIP_ID_KEY
import com.nicos.sampleandroidtvapp.ui.ship_details_screen.ShipDetailsScreen
import com.nicos.sampleandroidtvapp.ui.ships_screen.ShipsScreen
import com.nicos.sampleandroidtvapp.utils.screen_routes.Screens
import com.nicos.sampleandroidtvapp.utils.screen_routes.Screens.SHIP_DETAILS_SCREEN

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.LAUNCHER_SCREEN
    ) {
        composable(Screens.LAUNCHER_SCREEN) {
            LauncherScreen(navController = navController)
        }
        composable(Screens.SHIPS_SCREEN) {
            ShipsScreen(navController)
        }
        composable("${SHIP_DETAILS_SCREEN}/{$SHIP_ID_KEY}", arguments = listOf(
            navArgument(SHIP_ID_KEY) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })) {
            ShipDetailsScreen(shipId = it.arguments?.getString(SHIP_ID_KEY, null)!!)
        }
    }
}
