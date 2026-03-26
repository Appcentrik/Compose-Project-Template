package es.mobiledev.domain.usecase.article

import es.mobiledev.common.response.AsyncResult
import es.mobiledev.domain.gateway.article.ArticleGateway
import es.mobiledev.domain.model.article.ArticleResponseBo
import kotlinx.coroutines.flow.Flow

interface GetArticlesUseCase {
    suspend operator fun invoke(
        limit: Long,
        offset: Long
    ): Flow<AsyncResult<ArticleResponseBo>>
}

class GetArticlesUseCaseImpl(
    private val articleGateway: ArticleGateway,
) : GetArticlesUseCase {
    override suspend fun invoke(
        limit: Long,
        offset: Long
    ): Flow<AsyncResult<ArticleResponseBo>> = articleGateway.getArticles(limit = limit, offset = offset)
}
