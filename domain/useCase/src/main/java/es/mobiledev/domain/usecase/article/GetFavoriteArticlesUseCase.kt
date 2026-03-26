package es.mobiledev.domain.usecase.article

import es.mobiledev.common.response.AsyncResult
import es.mobiledev.domain.gateway.article.ArticleGateway
import es.mobiledev.domain.model.article.ArticleBo
import kotlinx.coroutines.flow.Flow

interface GetFavoriteArticlesUseCase {
    suspend operator fun invoke(): Flow<AsyncResult<List<ArticleBo>>>
}

class GetFavoriteArticlesUseCaseImpl(
    private val articleGateway: ArticleGateway,
) : GetFavoriteArticlesUseCase {
    override suspend fun invoke(): Flow<AsyncResult<List<ArticleBo>>> = articleGateway.getFavoriteArticles()
}
