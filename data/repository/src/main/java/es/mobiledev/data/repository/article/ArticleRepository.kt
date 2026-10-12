package es.mobiledev.data.repository.article

import es.mobiledev.common.response.AsyncResult
import es.mobiledev.data.source.article.ArticleLocalDataSource
import es.mobiledev.data.source.article.ArticleRemoteDataSource
import es.mobiledev.domain.gateway.article.ArticleGateway
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.model.article.ArticleResponseBo
import kotlinx.coroutines.flow.Flow

class ArticleRepository(
    private val remote: ArticleRemoteDataSource,
    private val local: ArticleLocalDataSource,
) : ArticleGateway {
    override suspend fun getArticles(
        limit: Long,
        offset: Long
    ): Flow<AsyncResult<ArticleResponseBo>> = remote.getArticles(limit = limit, offset = offset)

    override suspend fun getArticleById(id: Long): Flow<AsyncResult<ArticleBo>> = remote.getArticleById(id = id)

    override suspend fun getFavoriteArticles(): Flow<AsyncResult<List<ArticleBo>>> = local.getFavoriteArticles()

    override suspend fun isArticleFavorite(id: Long): Flow<AsyncResult<Boolean>> = local.isArticleFavorite(id = id)

    override suspend fun saveFavoriteArticle(articleBo: ArticleBo): AsyncResult<Unit> = local.saveFavoriteArticle(articleBo)

    override suspend fun removeFavoriteArticle(articleBo: ArticleBo): AsyncResult<Unit> = local.removeFavoriteArticle(articleBo)
}
