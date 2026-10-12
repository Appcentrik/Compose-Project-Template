package es.mobiledev.feature.articledetail.state

import es.mobiledev.domain.model.article.ArticleBo

data class ArticleDetailUiState(
    val article: ArticleBo? = null,
    val isFavorite: Boolean = false,
    val isTogglingFavorite: Boolean = false,
)
