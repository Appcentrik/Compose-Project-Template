package es.mobiledev.feature.home.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.mobiledev.common.response.AsyncResultException
import es.mobiledev.common.response.onResult
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.ui.base.BaseViewModel
import es.mobiledev.commonandroid.ui.base.UiState
import es.mobiledev.commonandroid.ui.component.error.UiError
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
    ) : BaseViewModel<HomeUiState>() {
        override val uiState: MutableStateFlow<UiState<HomeUiState>> =
            MutableStateFlow(value = UiState(data = HomeUiState()))

        private val favoriteMutex = Mutex()

        init {
            viewModelScope.launch(Dispatchers.IO) {
                fetchData()
            }
            observeFavoriteArticles()
        }

        suspend fun fetchData() {
            uiState.loadingState()
            getArticles {
                viewModelScope.launch {
                    getLastOpenTime()
                }
            }
        }

        private suspend fun getArticles(onSuccess: () -> Unit) {
            getArticlesUseCase(limit = 5L, offset = 0L).onResult(
                onSuccess = {
                    uiState.successState { currentUiState ->
                        currentUiState.copy(
                            articles = it.results,
                        )
                    }
                    onSuccess()
                },
                onError = { error ->
                    uiState.errorState(
                        uiError =
                            error.toUiError<UiError.SnackBar> {
                                viewModelScope.launch {
                                    fetchData()
                                }
                            },
                    )
                    logAppError(error)
                },
            )
        }

        private suspend fun saveLastOpenTime() = saveLastOpenTimeUseCase(timeInMillis = getCurrentEpochMilli())

        private suspend fun getLastOpenTime() {
            val lastOpenTime = getLastOpenTimeUseCase().first()
            Log.d("HomeViewModel", "Last open time: ${Date(lastOpenTime)}")
            saveLastOpenTime()
        }

        private fun observeFavoriteArticles() {
            viewModelScope.launch(Dispatchers.IO) {
                uiState.updateState { currentUiState ->
                    currentUiState.copy(isLoadingFavorites = true)
                }
                getFavoriteArticlesUseCase().onResult(
                    onSuccess = { articles ->
                        uiState.updateState { currentUiState ->
                            currentUiState.copy(
                                favoriteArticles = articles,
                                isLoadingFavorites = false,
                            )
                        }
                        uiState.updateErrorState(UiError.None)
                    },
                    onError = { error ->
                        uiState.updateState { currentUiState ->
                            currentUiState.copy(isLoadingFavorites = false)
                        }
                        uiState.updateErrorState(
                            error.toUiError<UiError.SnackBar> {
                                observeFavoriteArticles()
                            },
                        )
                        logAppError(error)
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
                    uiState.updateState { currentUiState ->
                        currentUiState.copy(isTogglingFavorite = true)
                    }
                    try {
                        saveOrRemoveFavoriteArticleUseCase(
                            article = article,
                            isFavorite = isFavorite,
                        )
                        uiState.updateState { currentUiState ->
                            currentUiState.copy(isTogglingFavorite = false)
                        }
                        uiState.updateErrorState(UiError.None)
                    } catch (error: AsyncResultException) {
                        uiState.updateState { currentUiState ->
                            currentUiState.copy(isTogglingFavorite = false)
                        }
                        uiState.updateErrorState(
                            error.error.toUiError<UiError.SnackBar> {
                                onFavoriteClick(article, isFavorite)
                            },
                        )
                        logAppError(error.error)
                    } catch (error: Throwable) {
                        uiState.updateState { currentUiState ->
                            currentUiState.copy(isTogglingFavorite = false)
                        }
                        uiState.updateErrorState(
                            UiError.SnackBar(
                                title = R.string.error_unknown_title,
                                message = R.string.error_unknown_message,
                                action = {
                                    onFavoriteClick(article, isFavorite)
                                },
                            ),
                        )
                        Log.e("HomeViewModel", "Unexpected error toggling favorite", error)
                    }
                }
            }
        }
    }
