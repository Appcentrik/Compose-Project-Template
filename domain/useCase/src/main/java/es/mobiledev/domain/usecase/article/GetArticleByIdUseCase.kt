package es.mobiledev.domain.usecase.article

import es.mobiledev.common.response.AsyncResult
import es.mobiledev.domain.gateway.article.ArticleGateway
import es.mobiledev.domain.model.article.ArticleBo
import kotlinx.coroutines.flow.Flow

interface GetArticleByIdUseCase {
    suspend operator fun invoke(
        id: Long,
    ): Flow<AsyncResult<ArticleBo>>
}

class GetArticleByIdUseCaseImpl(
    private val articleGateway: ArticleGateway,
) : GetArticleByIdUseCase {
    override suspend fun invoke(
        id: Long,
    ): Flow<AsyncResult<ArticleBo>> = articleGateway.getArticleById(id = id)
}
