package es.mobiledev.feature.home.state

import es.mobiledev.domain.model.article.ArticleBo

data class HomeUiState(
    val isLoadingFavorites: Boolean = false,
    val isTogglingFavorite: Boolean = false,
    val articles: List<ArticleBo> = emptyList(),
    val favoriteArticles: List<ArticleBo> = emptyList(),
)
