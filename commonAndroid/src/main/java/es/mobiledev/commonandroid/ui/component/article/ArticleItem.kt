package es.mobiledev.commonandroid.ui.component.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import coil.compose.AsyncImage
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.model.article.mockListArticles

@Composable
fun ArticleItem(
    article: ArticleBo,
    isFavorite: Boolean,
    isTogglingFavorite: Boolean = false,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier =
            Modifier
                .clickable { onItemClick() }
                .padding(dimensionResource(R.dimen.article_item__content_padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.article_item__vertical_arrangement))
    ) {
        article.imageUrl.takeIf { it.isNotEmpty() }?.let {
            AsyncImage(
                contentScale = ContentScale.Crop,
                model = article.imageUrl,
                contentDescription = null,
                modifier = Modifier.height(dimensionResource(R.dimen.article_item__image_height)),
            )
        }
        Text(text = article.title, style = MaterialTheme.typography.headlineSmall)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = article.newsSite)
            IconButton(
                modifier = Modifier.size(dimensionResource(R.dimen.article_item__icon_button_height)),
                onClick = { onFavoriteClick() },
                enabled = !isTogglingFavorite,
                colors =
                    IconButtonDefaults.iconButtonColors(
                        contentColor =
                            if (isFavorite) {
                                Color(0xFFF9A825)
                            } else {
                                Color.Gray
                            }
                    )
            ) {
                Icon(
                    painter =
                        painterResource(
                            if (isFavorite) {
                                R.drawable.ic_cpt_favorites_filled
                            } else {
                                R.drawable.ic_cpt_favorites_outlined
                            }
                        ),
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.article_item__icon_size))
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun Preview() {
    CPTTheme {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(mockListArticles) { article ->
                ArticleItem(
                    article = article,
                    isFavorite = false,
                    onItemClick = {},
                    onFavoriteClick = {}
                )
            }
        }
    }
}
