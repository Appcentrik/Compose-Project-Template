package es.mobiledev.feature.home.state

import es.mobiledev.domain.model.article.ArticleBo

data class HomeUiState(
    val articles: List<ArticleBo> = emptyList(),
    val favoriteArticles: List<ArticleBo> = emptyList(),
    val togglingFavoriteIds: Set<Long> = emptySet(),
)
