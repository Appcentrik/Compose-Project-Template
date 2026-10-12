package es.mobiledev.feature.home.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.mobiledev.common.response.AsyncResult
import es.mobiledev.common.response.onResult
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
import kotlinx.coroutines.Dispatchers
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
    ) : BaseViewModel<HomeUiState>(appLogger) {
        override val mutableUiState: MutableStateFlow<UiState<HomeUiState>> =
            MutableStateFlow(value = UiState(data = HomeUiState()))

        private val favoriteMutex = Mutex()

        init {
            fetchArticles()
            observeFavoriteArticles()
        }

        private fun fetchArticles() {
            setLoading()
            viewModelScope.launch(Dispatchers.IO) {
                getArticlesUseCase(limit = 5L, offset = 0L).onResult(
                    onSuccess = { response ->
                        showSuccess { currentUiState ->
                            currentUiState.copy(articles = response.results)
                        }
                        viewModelScope.launch { trackLastOpenTime() }
                    },
                    onError = { error ->
                        showError(
                            uiError = error.toUiError(UiErrorPresentation.Screen),
                            action = ::fetchArticles,
                        )
                        logAppError(error)
                    },
                )
            }
        }

        private suspend fun saveLastOpenTime() = saveLastOpenTimeUseCase(timeInMillis = getCurrentEpochMilli())

        private suspend fun trackLastOpenTime() {
            val lastOpenTime = getLastOpenTimeUseCase().first()
            android.util.Log.d("HomeViewModel", "Last open time: ${Date(lastOpenTime)}")
            saveLastOpenTime()
        }

        /**
         * Long lived observer re-emitting on every database change. It never touches the error
         * channels on success, doing so used to wipe errors raised by the article request running
         * in parallel.
         */
        private fun observeFavoriteArticles() {
            viewModelScope.launch(Dispatchers.IO) {
                getFavoriteArticlesUseCase().onResult(
                    onSuccess = { articles ->
                        updateData { currentUiState ->
                            currentUiState.copy(favoriteArticles = articles)
                        }
                    },
                    onError = { error ->
                        showSnackBarError(
                            error = error,
                            action = ::observeFavoriteArticles,
                        )
                    },
                )
            }
        }

        fun onFavoriteClick(
            article: ArticleBo,
            isFavorite: Boolean,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                favoriteMutex.withLock {
                    updateData { currentUiState ->
                        currentUiState.copy(isTogglingFavorite = true)
                    }

                    val result =
                        saveOrRemoveFavoriteArticleUseCase(
                            article = article,
                            isFavorite = isFavorite,
                        )

                    updateData { currentUiState ->
                        currentUiState.copy(isTogglingFavorite = false)
                    }

                    when (result) {
                        is AsyncResult.Success ->
                            Unit

                        is AsyncResult.Error ->
                            showSnackBarError(
                                error = result.error,
                                action = { onFavoriteClick(article, isFavorite) },
                            )
                    }
                }
            }
        }
    }
