package es.mobiledev.data.local.article.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.mobiledev.data.local.article.dbo.ArticleDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavoriteArticle(article: ArticleDbo)

    @Delete
    suspend fun removeFavoriteArticle(article: ArticleDbo)

    @Query("SELECT * FROM articles")
    fun getFavoriteArticles(): Flow<List<ArticleDbo>>

    @Query("SELECT * FROM articles WHERE id = :articleId")
    suspend fun getFavoriteArticleById(articleId: Long): ArticleDbo?
}
