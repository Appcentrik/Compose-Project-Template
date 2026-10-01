package es.mobiledev.feature.articledetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import es.mobiledev.common.response.AsyncResultException
import es.mobiledev.common.response.onResult
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.ui.base.BaseViewModel
import es.mobiledev.commonandroid.ui.base.UiState
import es.mobiledev.commonandroid.ui.component.error.UiError
import es.mobiledev.commonandroid.ui.component.error.toUiError
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.usecase.article.GetArticleByIdUseCase
import es.mobiledev.domain.usecase.article.IsArticleFavoriteUseCase
import es.mobiledev.domain.usecase.article.SaveOrRemoveFavoriteArticleUseCase
import es.mobiledev.feature.articledetail.state.ArticleDetailUiState
import es.mobiledev.navigation.AppScreens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel
    @Inject
    constructor(
        private val getArticleByIdUseCase: GetArticleByIdUseCase,
        private val isArticleFavoriteUseCase: IsArticleFavoriteUseCase,
        private val saveOrRemoveFavoriteArticleUseCase: SaveOrRemoveFavoriteArticleUseCase,
        savedStateHandle: SavedStateHandle,
    ) : BaseViewModel<ArticleDetailUiState>() {
        val args = savedStateHandle.toRoute<AppScreens.ArticleDetail>()
        override val uiState: MutableStateFlow<UiState<ArticleDetailUiState>> =
            MutableStateFlow(value = UiState(data = ArticleDetailUiState()))

        private val favoriteMutex = Mutex()

        init {
            viewModelScope.launch(Dispatchers.IO) {
                fetchData()
            }
        }

        private suspend fun fetchData() {
            uiState.loadingState()
            supervisorScope {
                val favoriteJob = launch { isArticleFavorite(args.id) }
                val articleJob = launch { getArticle(args.id) }
                favoriteJob.join()
                articleJob.join()
            }
        }

        private fun retryFavoriteCheck() {
            viewModelScope.launch(Dispatchers.IO) {
                isArticleFavorite(args.id)
            }
        }

        private suspend fun isArticleFavorite(id: Long) =
            isArticleFavoriteUseCase(id = id).onResult(
                onSuccess = {
                    uiState.updateState { currentUiState ->
                        currentUiState.copy(
                            isFavorite = it,
                        )
                    }
                },
                onError = { error ->
                    uiState.updateErrorState(
                        error.toUiError<UiError.SnackBar>(::retryFavoriteCheck),
                    )
                    logAppError(error)
                },
            )

        private suspend fun getArticle(id: Long) =
            getArticleByIdUseCase(id = id).onResult(
                onSuccess = {
                    uiState.successState { currentUiState ->
                        currentUiState.copy(
                            article = it,
                        )
                    }
                },
                onError = { error ->
                    uiState.errorState(
                        error.toUiError<UiError.Screen> {
                            viewModelScope.launch {
                                fetchData()
                            }
                        },
                    )
                    logAppError(error)
                },
            )

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
                            currentUiState.copy(
                                isFavorite = !isFavorite,
                                isTogglingFavorite = false,
                            )
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
                        android.util.Log.e("ArticleDetailViewModel", "Unexpected error toggling favorite", error)
                    }
                }
            }
        }
    }
