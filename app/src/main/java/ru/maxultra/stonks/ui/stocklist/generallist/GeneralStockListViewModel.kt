package ru.maxultra.stonks.ui.stocklist.generallist

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.maxultra.stonks.data.database.StockDatabase
import ru.maxultra.stonks.data.network.StonksNetwork
import ru.maxultra.stonks.data.repository.StockRepository
import ru.maxultra.stonks.ui.base.BaseViewModel
import ru.maxultra.stonks.util.Status

class GeneralStockListViewModel(
    private val stockRepository: StockRepository
) : BaseViewModel(stockRepository) {
    val stocks = stockRepository.getAllStocks()

    private val _stockListStatus = MutableLiveData(Status.LOADING)
    val stockListStatus: LiveData<Status>
        get() = _stockListStatus

    // FIXME: All the logic below should be revised
    init {
        fetchStockList()
        refreshStockList()
    }

    fun getStockList() {
        if (stocks.value.isNullOrEmpty()) {
            Log.d("GeneralStockListVM", "List is empty")
            fetchStockList()
        } else {
            Log.d("GeneralStockListVM", "List is not empty (${stocks.value!!.size} elements)")
            refreshStockList()
        }
    }

    private fun fetchStockList() = viewModelScope.launch {
        try {
            _stockListStatus.value = Status.LOADING
            stockRepository.fetchStockList()
            _stockListStatus.value = Status.SUCCESS
        } catch (e: Exception) {
            _stockListStatus.value = Status.ERROR
        }
    }

    private fun refreshStockList() = viewModelScope.launch {
        try {
            _stockListStatus.value = Status.LOADING
            stocks.value?.let { list ->
                Log.d("GeneralStockListVM", "Updating profiles (${list.size})")
                stockRepository.updateProfiles(list.map { it.ticker })
            }
            _stockListStatus.value = Status.SUCCESS
        } catch (e: Exception) {
            _stockListStatus.value = Status.ERROR
        }
    }
}

@Suppress("UNCHECKED_CAST")
class GeneralStockListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GeneralStockListViewModel::class.java)) {
            val dao = StockDatabase.getDatabase(context).stockDao
            val service = StonksNetwork.service
            val repository = StockRepository(dao, service)
            val viewModel = GeneralStockListViewModel(repository)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
