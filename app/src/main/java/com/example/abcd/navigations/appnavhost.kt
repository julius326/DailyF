package com.example.abcd.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.abcd.ui.theme.SCREENS.AddMotivationScreen
import com.example.abcd.ui.theme.SCREENS.HabitListScreen
import com.example.abcd.ui.theme.SCREENS.HabitScreen
import com.example.abcd.ui.theme.SCREENS.HomeScreen
import com.example.abcd.ui.theme.SCREENS.LoginScreen
import com.example.abcd.ui.theme.SCREENS.MotivationScreen
import com.example.abcd.ui.theme.SCREENS.RegisterScreen
import com.example.abcd.ui.theme.SCREENS.SplashScreen
import com.example.abcd.ui.theme.SCREENS.TodayTaskScreen
import com.example.abcd.ui.theme.SCREENS.TomorrowTaskScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = ROUTE_SPLASH) {
        composable(ROUTE_LOGIN) { LoginScreen(navController = navController) }
        composable(ROUTE_REGISTER) { RegisterScreen(navController =navController) }
        composable(ROUTE_HOME) { HomeScreen(navController = navController) }
        composable(ROUTE_MOTIVATION) { MotivationScreen(navController = navController) }
        composable(ROUTE_HABITS) { HabitScreen(navController = navController) }
        composable(ROUTE_HABITLIST) { HabitListScreen(navController = navController) }
        composable(ROUTE_TOMORROW) { TomorrowTaskScreen(navController = navController) }
        composable (ROUTE_ADDMOTIVATIONS ) { AddMotivationScreen(navController = navController) }
        composable (ROUTE_SPLASH){ SplashScreen(navController = navController) }
        composable (ROUTE_TODAY){ TodayTaskScreen(navController = navController) }
    }
}
