package es.mobiledev.data.local.article.datasource

import es.mobiledev.data.local.AppRoomDatabase
import es.mobiledev.data.local.article.dao.toBo
import es.mobiledev.data.local.article.dao.toDbo
import es.mobiledev.data.local.article.error.catchLocalError
import es.mobiledev.data.source.article.ArticleLocalDataSource
import es.mobiledev.domain.model.article.ArticleBo

class ArticleLocalDataSourceImpl(
    roomDatabase: AppRoomDatabase,
) : ArticleLocalDataSource {
    private val articleDao = roomDatabase.articleDao()

    override suspend fun saveFavoriteArticle(article: ArticleBo) = articleDao.saveFavoriteArticle(article.toDbo())

    override suspend fun removeFavoriteArticle(article: ArticleBo) = articleDao.removeFavoriteArticle(article.toDbo())

    override suspend fun getFavoriteArticles(): List<ArticleBo> =
        catchLocalError {
            articleDao.getFavoriteArticles().map { it.toBo() }
        }

    override suspend fun isArticleFavorite(id: Long): Boolean =
        catchLocalError {
            articleDao.getFavoriteArticleById(articleId = id) != null
        }
}
