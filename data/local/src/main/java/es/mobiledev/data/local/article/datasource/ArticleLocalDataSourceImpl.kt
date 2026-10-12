package es.mobiledev.data.local.article.datasource

import es.mobiledev.common.response.AsyncResult
import es.mobiledev.common.util.AppDispatchers
import es.mobiledev.data.local.AppRoomDatabase
import es.mobiledev.data.local.article.dao.toBo
import es.mobiledev.data.local.article.dao.toDbo
import es.mobiledev.data.local.error.localResponse
import es.mobiledev.data.source.article.ArticleLocalDataSource
import es.mobiledev.domain.model.article.ArticleBo
import kotlinx.coroutines.flow.Flow

class ArticleLocalDataSourceImpl(
    roomDatabase: AppRoomDatabase,
    private val appDispatchers: AppDispatchers,
) : ArticleLocalDataSource {
    private val articleDao = roomDatabase.articleDao()

    override suspend fun saveFavoriteArticle(article: ArticleBo): AsyncResult<Unit> =
        localResponse(appDispatchers) {
            articleDao.saveFavoriteArticle(article.toDbo())
        }.value()

    override suspend fun removeFavoriteArticle(article: ArticleBo): AsyncResult<Unit> =
        localResponse(appDispatchers) {
            articleDao.removeFavoriteArticle(article.toDbo())
        }.value()

    override suspend fun getFavoriteArticles(): Flow<AsyncResult<List<ArticleBo>>> =
        localResponse(appDispatchers) {
            articleDao.getFavoriteArticles().map { it.toBo() }
        }.flow()

    override suspend fun isArticleFavorite(id: Long): Flow<AsyncResult<Boolean>> =
        localResponse(appDispatchers) {
            articleDao.getFavoriteArticleById(articleId = id) != null
        }.flow()
}
