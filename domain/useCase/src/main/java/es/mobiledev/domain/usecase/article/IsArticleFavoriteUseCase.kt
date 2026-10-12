package es.mobiledev.domain.usecase.article

import es.mobiledev.common.response.AsyncResult
import kotlinx.coroutines.flow.Flow
import es.mobiledev.domain.gateway.article.ArticleGateway

interface IsArticleFavoriteUseCase {
    suspend operator fun invoke(
        id: Long,
    ): Flow<AsyncResult<Boolean>>
}

class IsArticleFavoriteUseCaseImpl(
    private val articleGateway: ArticleGateway,
) : IsArticleFavoriteUseCase {
    override suspend fun invoke(
        id: Long,
    ): Flow<AsyncResult<Boolean>> = articleGateway.isArticleFavorite(id)
}
