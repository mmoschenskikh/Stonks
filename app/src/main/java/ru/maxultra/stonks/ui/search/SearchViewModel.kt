package ru.maxultra.stonks.ui.search

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.maxultra.stonks.data.database.StockDatabase
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.data.network.StonksNetwork
import ru.maxultra.stonks.data.repository.StockRepository

class SearchViewModel(private val stockRepository: StockRepository) : ViewModel() {
    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    private val _searchResult = MutableLiveData<List<Stock>>()
    val searchResult: LiveData<List<Stock>>
        get() = _searchResult

    private val _navigateToSearchFragment = MutableLiveData(false)
    val navigateToSearchFragment: LiveData<Boolean>
        get() = _navigateToSearchFragment

    private val _navigateUp = MutableLiveData(false)
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    private var searchJob: Job? = null

    fun updateQuery(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        search()
    }

    fun search() {
        searchJob = viewModelScope.launch {
            searchQuery.value?.let {
                if (it.isNotBlank()) _searchResult.postValue(stockRepository.search(it))
            }
        }
    }

    fun onFavouriteClicked(stock: Stock) = viewModelScope.launch {
        stockRepository.toggleFavourite(stock)
    }

    fun onNavigateToSearchFragment() {
        _navigateToSearchFragment.value = true
    }

    fun onNavigationToSearchFragmentFinished() {
        _navigateToSearchFragment.value = false
    }

    fun onNavigateUp() {
        _navigateUp.value = true
    }

    fun onNavigationUpFinished() {
        _navigateUp.value = false
    }
}

class SearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            val dao = StockDatabase.getDatabase(context).stockDao
            val service = StonksNetwork.service
            val repository = StockRepository(dao, service)
            val viewModel = SearchViewModel(repository)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
