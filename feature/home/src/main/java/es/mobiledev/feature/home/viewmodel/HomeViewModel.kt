package es.mobiledev.feature.home.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.mobiledev.common.response.onResult
import es.mobiledev.common.util.AppDispatchers
import es.mobiledev.commonandroid.core.logging.AppLogger
import es.mobiledev.commonandroid.ui.base.BaseViewModel
import es.mobiledev.commonandroid.ui.base.UiState
import es.mobiledev.commonandroid.ui.component.error.UiErrorPresentation
import es.mobiledev.commonandroid.ui.component.error.toUiError
import es.mobiledev.commonandroid.util.getCurrentEpochMilli
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.usecase.article.GetArticlesUseCase
import es.mobiledev.domain.usecase.article.GetFavoriteArticlesUseCase
import es.mobiledev.domain.usecase.article.SaveOrRemoveFavoriteArticleUseCase
import es.mobiledev.domain.usecase.preferences.GetLastOpenTimeUseCase
import es.mobiledev.domain.usecase.preferences.SaveLastOpenTimeUseCase
import es.mobiledev.feature.home.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getArticlesUseCase: GetArticlesUseCase,
        private val getFavoriteArticlesUseCase: GetFavoriteArticlesUseCase,
        private val saveOrRemoveFavoriteArticleUseCase: SaveOrRemoveFavoriteArticleUseCase,
        private val saveLastOpenTimeUseCase: SaveLastOpenTimeUseCase,
        private val getLastOpenTimeUseCase: GetLastOpenTimeUseCase,
        appLogger: AppLogger,
        appDispatchers: AppDispatchers,
    ) : BaseViewModel<HomeUiState>(appLogger, appDispatchers) {
        override val mutableUiState: MutableStateFlow<UiState<HomeUiState>> =
            MutableStateFlow(value = UiState(data = HomeUiState()))

        private val favoriteMutex = Mutex()

        fun onScreenResumed() {
            viewModelScope.launch(appDispatchers.io) {
                val articlesLoaded =
                    uiState.value.data.articles
                        .isNotEmpty()
                if (articlesLoaded) {
                    loadFavorites()
                } else {
                    loadArticles()
                }
            }
        }

        private suspend fun loadArticles() {
            setLoading()
            getArticlesUseCase(limit = 5L, offset = 0L).onResult(
                onSuccess = { response ->
                    showSuccess { currentUiState ->
                        currentUiState.copy(articles = response.results)
                    }
                    trackLastOpenTime()
                    loadFavorites()
                },
                onError = { error ->
                    showError(
                        uiError =
                            error.toUiError(
                                UiErrorPresentation.Screen,
                                action = ::onScreenResumed,
                            ),
                    )
                    logAppError(error)
                },
            )
        }

        private suspend fun saveLastOpenTime() = saveLastOpenTimeUseCase(timeInMillis = getCurrentEpochMilli())

        private suspend fun trackLastOpenTime() {
            val lastOpenTime = getLastOpenTimeUseCase().first()
            android.util.Log.d("HomeViewModel", "Last open time: ${Date(lastOpenTime)}")
            saveLastOpenTime()
        }

        private suspend fun loadFavorites() {
            getFavoriteArticlesUseCase().onResult(
                onSuccess = { articles ->
                    updateData { currentUiState ->
                        currentUiState.copy(favoriteArticles = articles)
                    }
                },
                onError = { error ->
                    showSnackBarError(
                        error = error,
                        action = ::onScreenResumed,
                    )
                    logAppError(error)
                },
            )
        }

        fun onFavoriteClick(
            article: ArticleBo,
            isFavorite: Boolean,
        ) {
            viewModelScope.launch(appDispatchers.io) {
                favoriteMutex.withLock {
                    if (article.id !in uiState.value.data.togglingFavoriteIds) {
                        toggleFavorite(article = article, isFavorite = isFavorite)
                    }
                }
            }
        }

        private suspend fun toggleFavorite(
            article: ArticleBo,
            isFavorite: Boolean,
        ) {
            updateData { currentUiState ->
                currentUiState.copy(togglingFavoriteIds = currentUiState.togglingFavoriteIds + article.id)
            }

            try {
                saveOrRemoveFavoriteArticleUseCase(
                    article = article,
                    isFavorite = isFavorite,
                ).onResult(
                    onSuccess = {
                        loadFavorites()
                    },
                    onError = { error ->
                        showSnackBarError(
                            error = error,
                            action = { onFavoriteClick(article, isFavorite) },
                        )
                        logAppError(error)
                    },
                )
            } finally {
                updateData { currentUiState ->
                    currentUiState.copy(togglingFavoriteIds = currentUiState.togglingFavoriteIds - article.id)
                }
            }
        }
    }
