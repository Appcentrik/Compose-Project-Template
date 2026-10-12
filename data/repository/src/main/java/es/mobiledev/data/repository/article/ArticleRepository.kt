package es.mobiledev.data.repository.article

import es.mobiledev.common.response.AsyncResult
import es.mobiledev.common.response.localResult
import es.mobiledev.common.response.localResultFlow
import es.mobiledev.common.response.remoteResultFlow
import es.mobiledev.data.source.article.ArticleLocalDataSource
import es.mobiledev.data.source.article.ArticleRemoteDataSource
import es.mobiledev.domain.gateway.article.ArticleGateway
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.model.article.ArticleResponseBo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArticleRepository(
    private val remote: ArticleRemoteDataSource,
    private val local: ArticleLocalDataSource,
) : ArticleGateway {
    override suspend fun getArticles(
        limit: Long,
        offset: Long
    ): Flow<AsyncResult<ArticleResponseBo>> =
        remoteResultFlow {
            remote.getArticles(limit = limit, offset = offset)
        }

    override suspend fun getArticleById(
        id: Long
    ): Flow<AsyncResult<ArticleBo>> =
        remoteResultFlow {
            remote.getArticleById(id = id)
        }

    override suspend fun getFavoriteArticles(): Flow<AsyncResult<List<ArticleBo>>> =
        localResultFlow {
            local.getFavoriteArticles()
        }

    override suspend fun isArticleFavorite(id: Long): Flow<AsyncResult<Boolean>> = flow { emit(localResult { local.isArticleFavorite(id) }) }

    override suspend fun saveFavoriteArticle(articleBo: ArticleBo): AsyncResult<Unit> =
        localResult {
            local.saveFavoriteArticle(articleBo)
        }

    override suspend fun removeFavoriteArticle(articleBo: ArticleBo): AsyncResult<Unit> =
        localResult {
            local.removeFavoriteArticle(articleBo)
        }
}
