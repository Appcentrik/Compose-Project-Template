package es.mobiledev.feature.articledetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import es.mobiledev.common.response.AsyncResult
import es.mobiledev.common.response.onResult
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
        appLogger: AppLogger,
    ) : BaseViewModel<ArticleDetailUiState>(appLogger) {
        val args = savedStateHandle.toRoute<AppScreens.ArticleDetail>()
        override val mutableUiState: MutableStateFlow<UiState<ArticleDetailUiState>> =
            MutableStateFlow(value = UiState(data = ArticleDetailUiState()))

        private val favoriteMutex = Mutex()

        private var fetchJob: Job? = null

        init {
            fetchData()
        }

        /**
         * supervisorScope so a failure in the favourite check does not cancel the article request.
         * Each one writes to its own error channel, so neither can hide the other.
         *
         * A previous run is cancelled first: retrying while a load is still in flight used to leave
         * two of them racing to write the same state.
         */
        private fun fetchData() {
            fetchJob?.cancel()
            setLoading()
            fetchJob =
                viewModelScope.launch(Dispatchers.IO) {
                    supervisorScope {
                        launch { loadFavoriteState() }
                        launch { loadArticle() }
                    }
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
                },
            )

        private suspend fun loadArticle() =
            getArticleByIdUseCase(id = args.id).onResult(
                onSuccess = { article ->
                    showSuccess { currentUiState ->
                        currentUiState.copy(article = article)
                    }
                },
                onError = { error ->
                    showError(
                        uiError = error.toUiError(UiErrorPresentation.Screen),
                        action = ::fetchData,
                    )
                    logAppError(error)
                },
            )

        private fun retryFavoriteCheck() {
            viewModelScope.launch(Dispatchers.IO) {
                loadFavoriteState()
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

                    when (result) {
                        is AsyncResult.Success ->
                            updateData { currentUiState ->
                                currentUiState.copy(isFavorite = !isFavorite)
                            }

                        is AsyncResult.Error ->
                            showSnackBarError(
                                error = result.error,
                                action = { onFavoriteClick(article, isFavorite) },
                            )
                    }

                    updateData { currentUiState ->
                        currentUiState.copy(isTogglingFavorite = false)
                    }
                }
            }
        }
    }
