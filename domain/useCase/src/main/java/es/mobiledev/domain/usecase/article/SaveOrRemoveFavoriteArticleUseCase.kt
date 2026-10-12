package es.mobiledev.domain.usecase.article

import es.mobiledev.common.response.AsyncResult
import es.mobiledev.domain.gateway.article.ArticleGateway
import es.mobiledev.domain.model.article.ArticleBo

interface SaveOrRemoveFavoriteArticleUseCase {
    suspend operator fun invoke(
        article: ArticleBo,
        isFavorite: Boolean
    ): AsyncResult<Unit>
}

class SaveOrRemoveFavoriteArticleUseCaseImpl(
    private val articleGateway: ArticleGateway,
) : SaveOrRemoveFavoriteArticleUseCase {
    override suspend fun invoke(
        article: ArticleBo,
        isFavorite: Boolean
    ): AsyncResult<Unit> =
        if (isFavorite) {
            articleGateway.removeFavoriteArticle(article)
        } else {
            articleGateway.saveFavoriteArticle(article)
        }
}
