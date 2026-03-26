package es.mobiledev.feature.home.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.mobiledev.common.response.onResult
import es.mobiledev.commonandroid.ui.base.BaseViewModel
import es.mobiledev.commonandroid.ui.base.UiState
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

        init {
            viewModelScope.launch(Dispatchers.IO) {
                fetchData()
            }
        }

        suspend fun fetchData() {
            uiState.loadingState()
            getArticles()
            getLastOpenTime()
        }

        private suspend fun getArticles() =
            getArticlesUseCase(limit = 5L, offset = 0L).onResult(
                onSuccess = {
                    uiState.successState { currentUiState ->
                        currentUiState.copy(
                            articles = it.results,
                        )
                    }
                },
                onError = { error ->
                    logAppError(error)
                }
            )

        private suspend fun saveLastOpenTime() = saveLastOpenTimeUseCase(timeInMillis = getCurrentEpochMilli())

        private suspend fun getLastOpenTime() =
            getLastOpenTimeUseCase().collectLatest { lastOpenTime ->
                Log.d("HomeViewModel", "Last open time: ${Date(lastOpenTime)}")
                saveLastOpenTime()
            }

        fun getFavoriteArticles() =
            viewModelScope.launch(Dispatchers.IO) {
                getFavoriteArticlesUseCase().onResult(
                    onSuccess = { articles ->
                        uiState.successState { currentUiState ->
                            currentUiState.copy(
                                favoriteArticles = articles,
                            )
                        }
                    },
                    onError = { error ->
                        logAppError(error)
                    },
                )
            }

        fun onFavoriteClick(
            article: ArticleBo,
            isFavorite: Boolean,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                saveOrRemoveFavoriteArticleUseCase(
                    article = article,
                    isFavorite = isFavorite,
                )
                getFavoriteArticles()
            }
        }
    }
