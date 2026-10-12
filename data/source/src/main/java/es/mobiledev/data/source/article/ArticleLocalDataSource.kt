package es.mobiledev.data.source.article

import es.mobiledev.domain.model.article.ArticleBo
import kotlinx.coroutines.flow.Flow

interface ArticleLocalDataSource {
    suspend fun saveFavoriteArticle(article: ArticleBo)

    suspend fun removeFavoriteArticle(article: ArticleBo)

    suspend fun getFavoriteArticles(): Flow<List<ArticleBo>>

    suspend fun isArticleFavorite(id: Long): Boolean
}
