package ru.maxultra.stonks.ui.stocklist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.maxultra.stonks.data.database.StockDatabase
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.data.network.StonksNetwork
import ru.maxultra.stonks.data.repository.StockRepository

class StockListViewModel(private val stockRepository: StockRepository) : ViewModel() {
    val stocks = stockRepository.getStocks().asLiveData(viewModelScope.coroutineContext)
    val favourite = stockRepository.getFavouriteStocks().asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            stockRepository.getStockList()
        }
    }

    fun onFavouriteClicked(stock: Stock) = viewModelScope.launch {
        stockRepository.toggleFavourite(stock)
    }
}

class StockListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockListViewModel::class.java)) {
            val dao = StockDatabase.getDatabase(context).stockDao
            val service = StonksNetwork.service
            val repository = StockRepository(dao, service)
            val viewModel = StockListViewModel(repository)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
