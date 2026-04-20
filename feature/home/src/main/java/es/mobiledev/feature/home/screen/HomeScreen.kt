package es.mobiledev.feature.home.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.base.BaseScreen
import es.mobiledev.commonandroid.ui.component.error.UiError
import es.mobiledev.feature.home.component.HomeScreenContent
import es.mobiledev.feature.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navigateToArticleDetail: (Long) -> Unit,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.getUiState().collectAsStateWithLifecycle()

    LifecycleStartEffect(Unit) {
        if (uiState.uiError is UiError.None) {
            viewModel.getFavoriteArticles()
        }
        onStopOrDispose { /* no-op */ }
    }

    BaseScreen(
        isLoading = uiState.isLoading,
        uiError = uiState.uiError,
    ) { paddingValues ->
        HomeScreenContent(
            uiState = uiState.data,
            onNavigateToDetail = { id ->
                navigateToArticleDetail(id)
            },
            onFavoriteClick = viewModel::onFavoriteClick,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Composable
@PreviewLightDark
fun HomeScreenPreview() {
    CPTTheme {
        HomeScreen(
            navigateToArticleDetail = { },
        )
    }
}
