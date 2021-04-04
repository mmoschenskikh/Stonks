package ru.maxultra.stonks.ui.stocklist.favouritelist

import android.content.Context
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.maxultra.stonks.data.database.StockDatabase
import ru.maxultra.stonks.data.network.StonksNetwork
import ru.maxultra.stonks.data.repository.StockRepository
import ru.maxultra.stonks.ui.base.BaseViewModel

class FavouriteStockListViewModel(
    stockRepository: StockRepository
) : BaseViewModel(stockRepository) {
    val favourite = stockRepository.getFavouriteStocks()
    val favouritesIsEmpty = Transformations.map(favourite) { it.isEmpty() }
}

@Suppress("UNCHECKED_CAST")
class FavouriteStockListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteStockListViewModel::class.java)) {
            val dao = StockDatabase.getDatabase(context).stockDao
            val service = StonksNetwork.service
            val repository = StockRepository(dao, service)
            val viewModel = FavouriteStockListViewModel(repository)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
