package ru.maxultra.stonks.ui.search

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.maxultra.stonks.data.database.StockDatabase
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.data.network.StonksNetwork
import ru.maxultra.stonks.data.repository.SearchRepository
import ru.maxultra.stonks.data.repository.StockRepository

class SearchViewModel(
    private val stockRepository: StockRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    private val _searchResult = MutableLiveData<List<Stock>>()
    val searchResult: LiveData<List<Stock>>
        get() = _searchResult

    private val _popularRequests = MutableLiveData<List<Stock>>()
    val popularRequests: LiveData<List<Stock>>
        get() = _popularRequests

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

    fun getPopularStocks() = viewModelScope.launch {
        _popularRequests.value = searchRepository.getPopularStocks()
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
            val stockDao = StockDatabase.getDatabase(context).stockDao
            val recentQueriesDao = StockDatabase.getDatabase(context).recentQueriesDao
            val service = StonksNetwork.service
            val stockRepository = StockRepository(stockDao, service)
            val searchRepository = SearchRepository(stockDao, recentQueriesDao, service)
            val viewModel = SearchViewModel(stockRepository, searchRepository)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
