package ru.maxultra.stonks.ui.details

import android.content.Context
import androidx.lifecycle.*
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import ru.maxultra.stonks.data.adapter
import ru.maxultra.stonks.data.database.StockDatabase
import ru.maxultra.stonks.data.network.StonksNetwork
import ru.maxultra.stonks.data.repository.StockRepository
import ru.maxultra.stonks.ui.base.BaseViewModel
import ru.maxultra.stonks.util.Status

class DetailsViewModel(
    private val stockRepository: StockRepository
) : BaseViewModel(stockRepository) {

    var stock = stockRepository.getStock("")
        private set

    private val _status = MutableLiveData(Status.LOADING)
    val status: LiveData<Status>
        get() = _status

    fun setTicker(ticker: String) {
        stock = stockRepository.getStock(ticker)
        refreshStock(ticker)
    }

    fun refreshStock(ticker: String) {
        viewModelScope.launch {
            try {
                _status.value = Status.LOADING
                stockRepository.fetchStock(ticker)
                stockRepository.fetchThirtyMinChart(ticker)
                _status.value = Status.SUCCESS
            } catch (e: Exception) {
                _status.value = Status.ERROR
            }
        }
    }

    fun getDisplayChart(dbChart: String?): List<Entry> {
        if (dbChart.isNullOrBlank()) return emptyList()
        return adapter.fromJson(dbChart)
            ?.map { Entry(it.date.toFloat(), it.price) }
            ?.reversed() ?: emptyList()
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
