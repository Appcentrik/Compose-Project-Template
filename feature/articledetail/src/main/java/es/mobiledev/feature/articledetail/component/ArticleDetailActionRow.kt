package es.mobiledev.feature.articledetail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.util.shareUrl
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.model.article.mockListArticles

@Composable
fun ArticleDetailActionRow(
    article: ArticleBo,
    isFavorite: Boolean,
    isTogglingFavorite: Boolean = false,
    onFavoriteClick: () -> Unit,
) {
    val context = LocalContext.current
    val iconResource =
        if (isFavorite) {
            R.drawable.ic_cpt_favorites_filled
        } else {
            R.drawable.ic_cpt_favorites_outlined
        }

    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = article.newsSite,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(weight = 1F)
        )
        IconButton(
            onClick = {
                context.shareUrl(url = article.url)
            },
            modifier = Modifier.size(size = dimensionResource(R.dimen.article_detail_action_row__icon_button__size)),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = null
            )
        }
        Spacer(
            modifier =
                Modifier
                    .width(width = dimensionResource(R.dimen.article_detail_action_row__spacer_width))
        )
        IconButton(
            onClick = onFavoriteClick,
            enabled = !isTogglingFavorite,
            colors =
                IconButtonDefaults.iconButtonColors(
                    contentColor =
                        if (isFavorite) {
                            Color(0xFFF9A825)
                        } else {
                            Color.Gray
                        }
                ),
            modifier = Modifier.size(size = dimensionResource(R.dimen.article_detail_action_row__icon_button__size)),
        ) {
            Icon(
                painter = painterResource(iconResource),
                contentDescription = null,
                modifier = Modifier.size(size = dimensionResource(R.dimen.article_detail_action_row__icon__size)),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun Preview() {
    CPTTheme {
        ArticleDetailActionRow(article = mockListArticles.first(), isFavorite = true, onFavoriteClick = {})
    }
}
