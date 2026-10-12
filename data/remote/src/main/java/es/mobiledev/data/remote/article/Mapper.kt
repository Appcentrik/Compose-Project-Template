package es.mobiledev.data.remote.article

import es.mobiledev.common.EMPTY_STRING
import es.mobiledev.common.INVALID_ID_LONG
import es.mobiledev.data.remote.article.dto.ArticleDto
import es.mobiledev.data.remote.article.dto.ArticleResponseDto
import es.mobiledev.data.remote.article.dto.AuthorDto
import es.mobiledev.data.remote.article.dto.SocialsDto
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.model.article.ArticleResponseBo
import es.mobiledev.domain.model.article.AuthorBo
import es.mobiledev.domain.model.article.SocialsBo

fun ArticleResponseDto.toBo() =
    ArticleResponseBo(
        count = count ?: INVALID_ID_LONG,
        next = next ?: EMPTY_STRING,
        previous = previous ?: EMPTY_STRING,
        results =
            results?.mapNotNull { articleDto ->
                articleDto.id?.let { articleDto.toBo() }
            } ?: emptyList()
    )

fun ArticleDto.toBo() =
    ArticleBo(
        id = id ?: INVALID_ID_LONG,
        title = title ?: EMPTY_STRING,
        authors = authors?.map { it.toBo() } ?: emptyList(),
        url = url ?: EMPTY_STRING,
        imageUrl = imageUrl ?: EMPTY_STRING,
        newsSite = newsSite ?: EMPTY_STRING,
        summary = summary ?: EMPTY_STRING,
        publishedAt = publishedAt ?: EMPTY_STRING,
        updatedAt = updatedAt ?: EMPTY_STRING,
    )

fun AuthorDto.toBo() =
    AuthorBo(
        name = name ?: EMPTY_STRING,
        socials = socials?.toBo()
    )

fun SocialsDto.toBo() =
    SocialsBo(
        youtube = youtube ?: EMPTY_STRING,
        x = x ?: EMPTY_STRING,
        mastodon = mastodon ?: EMPTY_STRING,
        bluesky = bluesky ?: EMPTY_STRING,
        instagram = instagram ?: EMPTY_STRING,
        linkedin = linkedin ?: EMPTY_STRING
    )
