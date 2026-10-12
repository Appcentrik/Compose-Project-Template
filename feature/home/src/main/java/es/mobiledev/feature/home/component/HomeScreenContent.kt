package es.mobiledev.feature.home.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.component.article.ArticleItem
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.feature.home.state.HomeUiState

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onNavigateToDetail: (Long) -> Unit,
    onFavoriteClick: (ArticleBo, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val favoriteIds by remember(uiState.favoriteArticles) {
        derivedStateOf { uiState.favoriteArticles.map { it.id }.toSet() }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(uiState.articles, key = { index, article -> article.id }) { index, article ->
            val isFavorite = article.id in favoriteIds
            ArticleItem(
                article = article,
                isFavorite = isFavorite,
                isTogglingFavorite = uiState.isTogglingFavorite,
                onItemClick = {
                    onNavigateToDetail(article.id)
                },
                onFavoriteClick = {
                    onFavoriteClick(article, isFavorite)
                }
            )
            if (index < uiState.articles.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun HomeScreenContentPreview() {
    CPTTheme {
        HomeScreenContent(
            uiState = HomeUiState(),
            onNavigateToDetail = {},
            onFavoriteClick = { _, _ -> }
        )
    }
}
