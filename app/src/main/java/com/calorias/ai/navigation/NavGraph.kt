package com.calorias.ai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calorias.ai.ui.HomeScreen
import com.calorias.ai.ui.ScannerScreen

object Routes {
    const val HOME = "home"
    const val SCANNER = "scanner"
}

@Composable
fun NavRoot(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(onScanClick = { navController.navigate(Routes.SCANNER) })
        }
        composable(Routes.SCANNER) {
            ScannerScreen(onBack = { navController.popBackStack() })
        }
    }
}
