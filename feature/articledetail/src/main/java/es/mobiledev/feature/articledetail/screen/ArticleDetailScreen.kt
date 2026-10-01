package es.mobiledev.feature.articledetail.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.base.BaseScreen
import es.mobiledev.feature.articledetail.component.ArticleDetailBottomBar
import es.mobiledev.feature.articledetail.component.ArticleDetailScreenContent
import es.mobiledev.feature.articledetail.viewmodel.ArticleDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen() {
    val viewModel: ArticleDetailViewModel = hiltViewModel()
    val uiState by viewModel.getUiState().collectAsStateWithLifecycle()

    BaseScreen(
        isLoading = uiState.isLoading,
        uiError = uiState.uiError,
        onUiErrorDismiss = viewModel::clearError,
        bottomBar = {
            uiState.data.article?.url?.let { safeUrl ->
                ArticleDetailBottomBar(
                    newsUrl = safeUrl,
                )
            }
        },
    ) { paddingValues ->
        uiState.data.article?.let { safeArticle ->
            ArticleDetailScreenContent(
                article = safeArticle,
                isFavorite = uiState.data.isFavorite,
                isTogglingFavorite = uiState.data.isTogglingFavorite,
                onFavoriteClick = {
                    viewModel.onFavoriteClick(
                        article = safeArticle,
                        isFavorite = uiState.data.isFavorite
                    )
                },
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}

@Composable
@PreviewLightDark
fun ArticleDetailScreenPreview() {
    CPTTheme {
        ArticleDetailScreen()
    }
}
