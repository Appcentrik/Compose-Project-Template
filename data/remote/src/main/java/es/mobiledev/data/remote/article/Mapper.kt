package es.mobiledev.data.remote.article

import es.mobiledev.data.remote.article.dto.ArticleDto
import es.mobiledev.data.remote.article.dto.ArticleResponseDto
import es.mobiledev.data.remote.article.dto.AuthorDto
import es.mobiledev.data.remote.article.dto.SocialsDto
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.model.article.ArticleResponseBo
import es.mobiledev.domain.model.article.AuthorBo
import es.mobiledev.domain.model.article.SocialsBo

// TODO: Add constants when common module is ready
fun ArticleResponseDto.toBo() =
    ArticleResponseBo(
        count = count ?: -1L,
        next = next ?: "",
        previous = previous ?: "",
        results =
            results?.mapNotNull { articleDto ->
                articleDto.id?.let { articleDto.toBo() }
            } ?: emptyList()
    )

fun ArticleDto.toBo() =
    ArticleBo(
        id = id ?: -1L,
        title = title ?: "",
        authors = authors?.map { it.toBo() } ?: emptyList(),
        url = url ?: "",
        imageUrl = imageUrl ?: "",
        newsSite = newsSite ?: "",
        summary = summary ?: "",
        publishedAt = publishedAt ?: "",
        updatedAt = updatedAt ?: "",
    )

fun AuthorDto.toBo() =
    AuthorBo(
        name = name ?: "",
        socials = socials?.toBo()
    )

fun SocialsDto.toBo() =
    SocialsBo(
        youtube = youtube ?: "",
        x = x ?: "",
        mastodon = mastodon ?: "",
        bluesky = bluesky ?: "",
        instagram = instagram ?: "",
        linkedin = linkedin ?: ""
    )
