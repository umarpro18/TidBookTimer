package com.sample.tidbooktimer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.sample.tidbooktimer.auth.SignInScreen
import com.sample.tidbooktimer.auth.SignInViewModel
import com.sample.tidbooktimer.auth.SignUpScreen
import com.sample.tidbooktimer.auth.SignUpViewModel
import com.sample.tidbooktimer.tidbookhome.TidBookTimerScreen
import com.sample.tidbooktimer.tidbookhome.TidBookTimerViewModel

@Composable
fun MyAppComposable() {
    val viewModel: MainActivityViewModel = hiltViewModel<MainActivityViewModel>()
    Scaffold(
        topBar = {},
        content = { padding ->
            AppGraph(
                modifier = Modifier.padding(padding),
                isUserLoggedIn = viewModel.isUserLogged.collectAsStateWithLifecycle().value
            )
        }
    )
}

@Composable
fun AppGraph(
    modifier: Modifier = Modifier,
    controller: NavHostController = rememberNavController(),
    isUserLoggedIn: Boolean
) {
    // Determine start destination based on whether the user is logged in
    val startDestination = if (isUserLoggedIn) {
        MyAppRoute.TidBookTimerHomeGraphRoute
    } else {
        MyAppRoute.AuthGraphRoute
    }

    NavHost(navController = controller, startDestination = startDestination) {
        navigation<MyAppRoute.AuthGraphRoute>(
            startDestination = MyAppRoute.SignInRoute
        ) {
            composable<MyAppRoute.SignInRoute> {
                val viewModel: SignInViewModel = hiltViewModel<SignInViewModel>()
                SignInScreen(
                    viewModel,
                    onNavigateToSignUp = { controller.navigate(MyAppRoute.SignUpRoute) },
                    onSignInSuccess = {
                        controller.navigate(MyAppRoute.TidBookTimerHomeGraphRoute) {
                            popUpTo(MyAppRoute.AuthGraphRoute) { inclusive = true }
                        }
                    }
                )
            }

            composable<MyAppRoute.SignUpRoute> { backStackEntry ->
                val viewModel: SignUpViewModel = hiltViewModel<SignUpViewModel>()
                SignUpScreen(viewModel, onSignUpSuccess = {
                    controller.navigate(MyAppRoute.TidBookTimerHomeGraphRoute) {
                        popUpTo(MyAppRoute.AuthGraphRoute) { inclusive = true }
                    }
                }, onSignInClicked = { controller.popBackStack() })
            }
        }

        navigation<MyAppRoute.TidBookTimerHomeGraphRoute>(
            startDestination = MyAppRoute.TidBookTimerHomeRoute,
        ) {
            composable<MyAppRoute.TidBookTimerHomeRoute> {
                val viewModel: TidBookTimerViewModel = hiltViewModel<TidBookTimerViewModel>()
                TidBookTimerScreen(
                    viewModel,
                    onLogOut = {
                        controller.navigate(MyAppRoute.AuthGraphRoute) {
                            popUpTo(MyAppRoute.TidBookTimerHomeGraphRoute) {
                                inclusive = true
                            }
                        }
                    })
            }
        }
    }
}
