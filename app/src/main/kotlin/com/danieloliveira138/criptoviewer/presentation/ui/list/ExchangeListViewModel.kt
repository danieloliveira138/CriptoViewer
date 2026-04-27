package com.danieloliveira138.criptoviewer.presentation.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danieloliveira138.criptoviewer.core.network.Result
import com.danieloliveira138.criptoviewer.domain.usecase.ExchangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeListViewModel @Inject constructor(
    private val exchangeUseCase: ExchangeUseCase,
) : ViewModel() {

    private companion object {
        const val PAGE_SIZE = 30
        const val FIRST_ELEMENT = 1
    }

    private val _state = MutableStateFlow(ExchangeListState())
    val state: StateFlow<ExchangeListState> = _state.asStateFlow()

    private val _effect = Channel<ExchangeListEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadExchanges(isRefresh = false)
    }

    fun onEvent(event: ExchangeListEvent) {
        when (event) {
            is ExchangeListEvent.Refresh -> loadExchanges(isRefresh = true)
            is ExchangeListEvent.LoadNextPage -> {
                val s = _state.value
                if (!s.isLoadingMore && !s.isLoading && s.hasMorePages) loadNextPage()
            }
            is ExchangeListEvent.OnExchangeClick -> sendEffect(
                ExchangeListEffect.NavigateTo("details/${event.exchange.id}")
            )
        }
    }

    private fun loadExchanges(isRefresh: Boolean) {
        viewModelScope.launch {
            _state.update {
                if (isRefresh) it.copy(isRefreshing = true, error = null)
                else it.copy(isLoading = true, error = null)
            }
            val result = exchangeUseCase.getExchangesList(
                start = FIRST_ELEMENT,
                limit = PAGE_SIZE
            )
            when (result) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            exchanges = result.data,
                            currentPage = 1,
                            hasMorePages = result.data.size >= PAGE_SIZE,
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.exception.message
                        )
                    }
                    sendEffect(
                        ExchangeListEffect.ShowToast(
                            result.exception.message ?: "Failed to load exchanges"))
                }
            }
        }
    }

    private fun loadNextPage() {
        viewModelScope.launch {
            val currentPage = _state.value.currentPage
            val start = currentPage * PAGE_SIZE + 1
            _state.update { it.copy(isLoadingMore = true) }
            val result = exchangeUseCase.getExchangesList(
                start = start,
                limit = PAGE_SIZE
            )
            when(result) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoadingMore = false,
                            exchanges = it.exchanges + result.data,
                            currentPage = currentPage + 1,
                            hasMorePages = result.data.size >= PAGE_SIZE,
                        )
                    }
                }
                is Result.Error -> {
                    val exceptionMessage = result.exception.message ?: "Failed to load more exchanges"
                    _state.update { it.copy(isLoadingMore = false, error = exceptionMessage) }
                    sendEffect(ExchangeListEffect.ShowToast(exceptionMessage))
                }
            }
        }
    }

    private fun sendEffect(effect: ExchangeListEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
