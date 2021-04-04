package ru.maxultra.stonks.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.data.repository.BaseRepository

abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel() {
    fun onFavouriteClicked(stock: Stock) = viewModelScope.launch {
        repository.toggleFavourite(stock)
    }
}
