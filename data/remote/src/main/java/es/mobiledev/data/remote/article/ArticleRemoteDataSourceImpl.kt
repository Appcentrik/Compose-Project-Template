package es.mobiledev.data.remote.article

import es.mobiledev.data.remote.error.catchRemoteError
import es.mobiledev.data.source.article.ArticleRemoteDataSource
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.model.article.ArticleResponseBo

class ArticleRemoteDataSourceImpl(
    val articleWs: ArticleWs
) : ArticleRemoteDataSource {
    override suspend fun getArticles(
        limit: Long,
        offset: Long
    ): ArticleResponseBo =
        catchRemoteError {
            articleWs.getArticles(limit, offset).toBo()
        }

    override suspend fun getArticleById(
        id: Long
    ): ArticleBo =
        catchRemoteError {
            articleWs.getArticleById(id).toBo()
        }
}
