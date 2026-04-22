package com.danieloliveira138.criptoviewer.presentation.ui.mainList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MainListViewModel @Inject constructor(
    private val exchangeUseCase: ExchangeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainListState())
    val state: StateFlow<MainListState> = _state.asStateFlow()

    private val _effect = Channel<MainListEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadExchanges(isRefresh = false)
    }

    fun onEvent(event: MainListEvent) {
        when (event) {
            is MainListEvent.Refresh -> loadExchanges(isRefresh = true)
            is MainListEvent.OnExchangeClick -> sendEffect(
                MainListEffect.NavigateTo("detail/${event.exchange.id}")
            )
        }
    }

    private fun loadExchanges(isRefresh: Boolean) {
        viewModelScope.launch {
            _state.update {
                if (isRefresh) it.copy(isRefreshing = true, error = null)
                else it.copy(isLoading = true, error = null)
            }
            try {
                val response = exchangeUseCase.getExchangesList(start = 1, limit = 100)
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        exchanges = response,
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, isRefreshing = false, error = e.message) }
                sendEffect(MainListEffect.ShowToast(e.message ?: "Failed to load exchanges"))
            }
        }
    }

    private fun sendEffect(effect: MainListEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
