package es.mobiledev.data.source.article

import es.mobiledev.common.response.AsyncResult
import es.mobiledev.domain.model.article.ArticleBo
import kotlinx.coroutines.flow.Flow

interface ArticleLocalDataSource {
    suspend fun saveFavoriteArticle(article: ArticleBo): AsyncResult<Unit>

    suspend fun removeFavoriteArticle(article: ArticleBo): AsyncResult<Unit>

    suspend fun getFavoriteArticles(): Flow<AsyncResult<List<ArticleBo>>>

    suspend fun isArticleFavorite(id: Long): Flow<AsyncResult<Boolean>>
}
