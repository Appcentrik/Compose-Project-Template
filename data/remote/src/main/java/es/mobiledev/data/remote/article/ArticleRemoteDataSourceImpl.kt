package es.mobiledev.data.remote.article

import es.mobiledev.common.response.AsyncResult
import es.mobiledev.common.util.AppDispatchers
import es.mobiledev.data.remote.error.remoteResponse
import es.mobiledev.data.source.article.ArticleRemoteDataSource
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.model.article.ArticleResponseBo
import kotlinx.coroutines.flow.Flow

class ArticleRemoteDataSourceImpl(
    private val articleWs: ArticleWs,
    private val appDispatchers: AppDispatchers,
) : ArticleRemoteDataSource {
    override suspend fun getArticles(
        limit: Long,
        offset: Long
    ): Flow<AsyncResult<ArticleResponseBo>> =
        remoteResponse(appDispatchers) {
            articleWs.getArticles(limit, offset).toBo()
        }.flow()

    override suspend fun getArticleById(
        id: Long
    ): Flow<AsyncResult<ArticleBo>> =
        remoteResponse(appDispatchers) {
            articleWs.getArticleById(id).toBo()
        }.flow()
}
