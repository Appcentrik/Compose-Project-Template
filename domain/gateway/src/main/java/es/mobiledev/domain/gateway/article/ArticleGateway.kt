package es.mobiledev.domain.gateway.article

import es.mobiledev.common.response.AsyncResult
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.model.article.ArticleResponseBo
import kotlinx.coroutines.flow.Flow

interface ArticleGateway {
    suspend fun getArticles(
        limit: Long,
        offset: Long,
    ): Flow<AsyncResult<ArticleResponseBo>>

    suspend fun getArticleById(
        id: Long
    ): Flow<AsyncResult<ArticleBo>>

    suspend fun getFavoriteArticles(): Flow<AsyncResult<List<ArticleBo>>>

    suspend fun saveFavoriteArticle(articleBo: ArticleBo)

    suspend fun removeFavoriteArticle(articleBo: ArticleBo)

    suspend fun isArticleFavorite(id: Long): Flow<AsyncResult<Boolean>>
}
