package es.mobiledev.commonandroid.ui.base

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.theme.BlueGrey50
import es.mobiledev.commonandroid.theme.BlueGrey700
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.component.error.UiError
import es.mobiledev.commonandroid.ui.component.error.UiErrorBottomSheet
import es.mobiledev.commonandroid.ui.component.error.UiErrorDialog
import es.mobiledev.commonandroid.ui.component.error.UiErrorScreen
import es.mobiledev.commonandroid.ui.component.error.UiErrorSnackbar
import es.mobiledev.commonandroid.ui.component.navigationBar.CptNavigationBar
import es.mobiledev.commonandroid.util.EmptyComposable
import es.mobiledev.navigation.NavigationModule

/**
 * CPT design component
 *
 * BaseScreen implements the basic structure of the visual design that fits the “edge-to-edge” design.
 *
 * This component provides a foundation to simplify the task of building your screen and managing its states.
 * @param modifier the [Modifier] to be applied to this component
 * @param isLoading a flag that indicates if the screen is loading
 * @param topBar top app bar of the screen, typically a [TopAppBar]
 * @param bottomBar bottom bar of the screen, typically a [NavigationBar]
 * @param content content of the screen. The lambda receives a [PaddingValues] that should be
 *   applied to the content root via [Modifier.padding] to properly offset top and bottom bars.
 *   If using [Modifier.verticalScroll], apply this modifier to the child of the scroll, and not
 *   on the scroll itself.
 */
@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = BlueGrey50,
    isLoading: Boolean = false,
    uiError: UiError = UiError.None,
    topBar: @Composable () -> Unit = EmptyComposable,
    bottomBar: @Composable () -> Unit = EmptyComposable,
    content: @Composable (PaddingValues) -> Unit = {},
) {
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        containerColor = backgroundColor,
        snackbarHost = {
            if (uiError is UiError.SnackBar) {
                UiErrorSnackbar(uiError)
            }
        },
        modifier =
            modifier
                .fillMaxSize(),
    ) { paddingValues ->
        content(paddingValues)
        Crossfade(targetState = isLoading || (uiError !is UiError.None && uiError !is UiError.SnackBar)) { needManageState ->
            if (needManageState) {
                if (isLoading) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            color = BlueGrey700,
                        )
                    }
                } else {
                    when (uiError) {
                        is UiError.Dialog -> UiErrorDialog(uiError, onDismiss = {})
                        is UiError.Screen -> UiErrorScreen(uiError)
                        is UiError.Sheet -> UiErrorBottomSheet(uiError, onDismiss = {})
                        is UiError.Embedded -> TODO()
                        else -> { // no-op
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewLightDark
private fun Preview() {
    CPTTheme {
        BaseScreen(
            topBar = {
                TopAppBar(
                    title = { Text("Preview") },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                )
            },
            bottomBar = {
                CptNavigationBar(
                    selectedModule = NavigationModule.HOME,
                    modifier = Modifier,
                    onClickModule = { /* no-op */ },
                )
            },
            uiError =
                UiError.SnackBar(
                    title = "Oops, it looks like there was a problem",
                    message = "An unexpected error occurred. Please try again later.",
                    action = {},
                ),
        ) { paddingValues ->
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
            ) {
                Text("Hello Android!")
            }
        }
    }
}
