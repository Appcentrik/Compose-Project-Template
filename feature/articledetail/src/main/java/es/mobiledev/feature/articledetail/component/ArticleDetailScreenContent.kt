package es.mobiledev.feature.articledetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.model.article.mockListArticles

@Composable
fun ArticleDetailScreenContent(
    article: ArticleBo,
    isFavorite: Boolean,
    isTogglingFavorite: Boolean = false,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement =
            Arrangement
                .spacedBy(space = dimensionResource(id = R.dimen.article_detail_screen__content__vertical_arrangement)),
        contentPadding = PaddingValues(all = dimensionResource(id = R.dimen.article_detail_screen__content__padding)),
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        item {
            ArticleDetailImage(article)
        }
        item {
            ArticleDetailActionRow(
                article = article,
                isFavorite = isFavorite,
                isTogglingFavorite = isTogglingFavorite,
                onFavoriteClick = onFavoriteClick
            )
        }
        item {
            ArticleDetailBody(article)
        }
    }
}

@Composable
@PreviewLightDark
private fun ArticleDetailScreenContentPreview() {
    CPTTheme {
        ArticleDetailScreenContent(
            article = mockListArticles.first(),
            isFavorite = true,
            onFavoriteClick = { }
        )
    }
}
