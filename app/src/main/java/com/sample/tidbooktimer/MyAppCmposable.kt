package com.sample.tidbooktimer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sample.tidbooktimer.auth.SignInScreen
import com.sample.tidbooktimer.auth.SignUpScreen
import com.sample.tidbooktimer.tidbookhome.TidBookTimerScreen

@Composable
fun MyAppComposable() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            /*val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute != MyAppRoute.SignInRoute && currentRoute != MyAppRoute.SignUpRoute) {
                MyAppTopBar(navController = navController)
            }*/
        },
        content = { padding ->
            AppGraph(modifier = Modifier.padding(padding), navController)
        }
    )
}

@Composable
fun AppGraph(
    modifier: Modifier = Modifier,
    controller: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = controller,
        startDestination = MyAppRoute.SignInRoute
    ) {
        composable<MyAppRoute.SignInRoute> {
            SignInScreen(navController = controller)
        }
        composable<MyAppRoute.SignUpRoute> {
            SignUpScreen(navController = controller)
        }
        composable<MyAppRoute.TidBookTimerHomeRoute> {
            TidBookTimerScreen(navController = controller)
        }
    }
}
