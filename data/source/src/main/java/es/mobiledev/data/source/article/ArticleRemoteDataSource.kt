package es.mobiledev.data.source.article

import es.mobiledev.common.response.AsyncResult
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.model.article.ArticleResponseBo
import kotlinx.coroutines.flow.Flow

interface ArticleRemoteDataSource {
    suspend fun getArticles(
        limit: Long,
        offset: Long
    ): Flow<AsyncResult<ArticleResponseBo>>

    suspend fun getArticleById(
        id: Long
    ): Flow<AsyncResult<ArticleBo>>
}
