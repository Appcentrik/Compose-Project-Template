package es.mobiledev.cpt

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import es.mobiledev.commonandroid.ui.base.ScreenWrapper
import es.mobiledev.commonandroid.ui.component.navigationBar.CptNavigationBar
import es.mobiledev.commonandroid.ui.component.topBar.CptTopBar
import es.mobiledev.cpt.ui.screen.testNavigation.TestScreen
import es.mobiledev.feature.articledetail.screen.ArticleDetailScreen
import es.mobiledev.feature.home.screen.HomeScreen
import es.mobiledev.feature.launcher.screen.LauncherScreen
import es.mobiledev.feature.webscreen.screen.WebScreen
import es.mobiledev.navigation.AppScreens
import es.mobiledev.navigation.util.getCurrentSelectedModule

/**
 * Application Navigation Graph
 *
 * This composable defines the main navigation graph of the application, handling the routing between different screens.
 * It also manages the visibility and state of the global UI elements like the TopBar and NavigationBar (BottomBar),
 * integrating them with the [ScreenWrapper].
 *
 * @param navController the [NavHostController] used to manage the app's navigation. Defaults to [rememberNavController].
 */
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    var currentScreen: AppScreens by remember { mutableStateOf(AppScreens.Launcher) }
    val currentSelectedModule by currentScreen.getCurrentSelectedModule().collectAsStateWithLifecycle()
    val showTopAppBar by remember { derivedStateOf { currentScreen.hasTopBar } }
    val showBottomBar by remember { derivedStateOf { currentScreen.hasBottomBar } }

    ScreenWrapper(
        topBar = { CptTopBar() },
        bottomBar = {
            CptNavigationBar(
                selectedModule = currentSelectedModule,
                modifier = Modifier,
                onClickModule = { screen ->
                    navController.navigate(screen) {
                        popUpTo(screen) {
                            inclusive = true
                        }
                    }
                },
            )
        },
        showTopAppBar = showTopAppBar,
        showBottomBar = showBottomBar,
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.Launcher,
            modifier =
                Modifier
                    .consumeWindowInsets(paddingValues)
                    .padding(paddingValues)
        ) {
            composable<AppScreens.Launcher> { navBackStackEntry ->
                currentScreen = navBackStackEntry.toRoute<AppScreens.Launcher>()
                LauncherScreen(
                    onLauncherFinished = {
                        navController.navigate(
                            route = AppScreens.Home,
                            builder = {
                                popUpTo(navBackStackEntry.destination.id) {
                                    inclusive = true
                                }
                            },
                        )
                    },
                )
            }
            composable<AppScreens.Home> { navBackStackEntry ->
                currentScreen = navBackStackEntry.toRoute<AppScreens.Home>()
                HomeScreen(
                    navigateToArticleDetail = { id ->
                        navController.navigate(AppScreens.ArticleDetail(id))
                    },
                )
            }
            composable<AppScreens.Test> { navBackStackEntry ->
                currentScreen = navBackStackEntry.toRoute<AppScreens.Test>()
                TestScreen(
                    onNavigateToWebScreen = { url ->
                        navController.navigate(AppScreens.WebScreen(url = url))
                    }
                )
            }

            composable<AppScreens.ArticleDetail> { navBackStackEntry ->
                currentScreen = navBackStackEntry.toRoute<AppScreens.ArticleDetail>()
                ArticleDetailScreen()
            }

            composable<AppScreens.WebScreen> { navBackStackEntry ->
                currentScreen = navBackStackEntry.toRoute<AppScreens.WebScreen>()
                WebScreen(
                    onNavigateBack = navController::popBackStack
                )
            }
        }
    }
}
