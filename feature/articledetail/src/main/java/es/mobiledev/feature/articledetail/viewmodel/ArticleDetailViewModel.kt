package es.mobiledev.feature.articledetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import es.mobiledev.common.response.onResult
import es.mobiledev.common.util.AppDispatchers
import es.mobiledev.commonandroid.core.logging.AppLogger
import es.mobiledev.commonandroid.ui.base.BaseViewModel
import es.mobiledev.commonandroid.ui.base.UiState
import es.mobiledev.commonandroid.ui.component.error.UiErrorPresentation
import es.mobiledev.commonandroid.ui.component.error.toUiError
import es.mobiledev.domain.model.article.ArticleBo
import es.mobiledev.domain.usecase.article.GetArticleByIdUseCase
import es.mobiledev.domain.usecase.article.IsArticleFavoriteUseCase
import es.mobiledev.domain.usecase.article.SaveOrRemoveFavoriteArticleUseCase
import es.mobiledev.feature.articledetail.state.ArticleDetailUiState
import es.mobiledev.navigation.AppScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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
        appLogger: AppLogger,
        appDispatchers: AppDispatchers,
    ) : BaseViewModel<ArticleDetailUiState>(appLogger, appDispatchers) {
        val args = savedStateHandle.toRoute<AppScreens.ArticleDetail>()
        override val mutableUiState: MutableStateFlow<UiState<ArticleDetailUiState>> =
            MutableStateFlow(value = UiState(data = ArticleDetailUiState()))

        private val favoriteMutex = Mutex()

        init {
            fetchData()
        }

        private fun fetchData() {
            setLoading()
            viewModelScope.launch(appDispatchers.io) {
                getArticleByIdUseCase(id = args.id).onResult(
                    onSuccess = { article ->
                        showSuccess { currentUiState ->
                            currentUiState.copy(article = article)
                        }
                        loadFavoriteState()
                    },
                    onError = { error ->
                        showError(
                            uiError =
                                error.toUiError(
                                    presentation = UiErrorPresentation.Screen,
                                    action = ::fetchData,
                                ),
                        )
                        logAppError(error)
                    },
                )
            }
        }

        private suspend fun loadFavoriteState() =
            isArticleFavoriteUseCase(id = args.id).onResult(
                onSuccess = { isFavorite ->
                    updateData { currentUiState ->
                        currentUiState.copy(isFavorite = isFavorite)
                    }
                },
                onError = { error ->
                    showSnackBarError(
                        error = error,
                        action = ::retryFavoriteCheck,
                    )
                    logAppError(error)
                },
            )

        private fun retryFavoriteCheck() {
            viewModelScope.launch(appDispatchers.io) {
                loadFavoriteState()
            }
        }

        fun onFavoriteClick(
            article: ArticleBo,
            isFavorite: Boolean,
        ) {
            viewModelScope.launch(appDispatchers.io) {
                favoriteMutex.withLock {
                    if (!uiState.value.data.isTogglingFavorite) {
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
                currentUiState.copy(isTogglingFavorite = true)
            }

            try {
                saveOrRemoveFavoriteArticleUseCase(
                    article = article,
                    isFavorite = isFavorite,
                ).onResult(
                    onSuccess = {
                        updateData { currentUiState ->
                            currentUiState.copy(isFavorite = !isFavorite)
                        }
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
                    currentUiState.copy(isTogglingFavorite = false)
                }
            }
        }
    }
