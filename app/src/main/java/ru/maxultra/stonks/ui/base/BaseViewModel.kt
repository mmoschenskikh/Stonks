package ru.maxultra.stonks.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.maxultra.stonks.data.repository.BaseRepository

abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel() {
    fun onFavouriteClicked(ticker: String) = viewModelScope.launch {
        repository.toggleFavourite(ticker)
    }
}
