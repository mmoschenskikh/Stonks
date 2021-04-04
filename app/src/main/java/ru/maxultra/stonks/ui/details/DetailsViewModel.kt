package ru.maxultra.stonks.ui.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.maxultra.stonks.data.database.StockDatabase
import ru.maxultra.stonks.data.network.StonksNetwork
import ru.maxultra.stonks.data.repository.StockRepository
import ru.maxultra.stonks.ui.base.BaseViewModel

class DetailsViewModel(
    private val stockRepository: StockRepository
) : BaseViewModel(stockRepository) {

    var stock = stockRepository.getStock("")
        private set

    fun setTicker(ticker: String) {
        stock = stockRepository.getStock(ticker)
    }
}

@Suppress("UNCHECKED_CAST")
class DetailsViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            val dao = StockDatabase.getDatabase(context).stockDao
            val service = StonksNetwork.service
            val repository = StockRepository(dao, service)
            val viewModel = DetailsViewModel(repository)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
