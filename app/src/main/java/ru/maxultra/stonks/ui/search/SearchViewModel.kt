package ru.maxultra.stonks.ui.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.maxultra.stonks.data.database.StockDatabase
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.data.network.StonksNetwork
import ru.maxultra.stonks.data.repository.SearchRepository
import ru.maxultra.stonks.data.repository.StockRepository
import ru.maxultra.stonks.ui.base.BaseViewModel
import ru.maxultra.stonks.util.Status

class SearchViewModel(
    stockRepository: StockRepository,
    private val searchRepository: SearchRepository
) : BaseViewModel(searchRepository) {
    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    private val stocks = stockRepository.getAllStocks()
    val searchResult = MutableLiveData<Set<String>>(emptySet())

    val searchResultList = Transformations.map(stocks) { list ->
        val resultTickers = searchResult.value
        if (resultTickers.isNullOrEmpty()) {
            emptyList()
        } else {
            list.filter { it.ticker in resultTickers }
        }
    }

    private val _searchStatus = MutableLiveData(Status.LOADING)
    val searchStatus: LiveData<Status>
        get() = _searchStatus

    private val placeholder = Stock("", "")
    private val placeholderList = listOf(placeholder, placeholder)

    private val _popularRequests = MutableLiveData(placeholderList)
    val popularRequests: LiveData<List<Stock>>
        get() = _popularRequests

    private val _popularStatus = MutableLiveData(Status.LOADING)
    val popularStatus: LiveData<Status>
        get() = _popularStatus

    val recentRequests = searchRepository.getRecentQueries()
    val recentIsEmpty = Transformations.map(recentRequests) { it.isEmpty() }

    private val _navigateToSearchFragment = MutableLiveData(false)
    val navigateToSearchFragment: LiveData<Boolean>
        get() = _navigateToSearchFragment

    private val _navigateUp = MutableLiveData(false)
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    private var searchJob: Job? = null

    init {
        getPopularStocks()
    }

    fun updateQuery(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        search()
        Log.d("SearchViewModel", "Hello from updateQuery")
    }

    fun search() {
        Log.d("SearchViewModel", "Hello from search")
        searchJob = viewModelScope.launch {
            val query = searchQuery.value
            if (query.isNullOrBlank()) {
                _searchStatus.value = Status.SUCCESS
                Log.d("SearchViewModel", "Hello from query.isNullOrBlank")
            } else {
                Log.d("SearchViewModel", "Hello from else")
                search(query)
            }
        }
    }

    private suspend fun search(query: String) {
        try {
            _searchStatus.value = Status.LOADING
            delay(250L) // To decrease the number of network requests
            Log.d("SearchViewModel", "Hello from search($query)")
            searchResult.value = searchRepository.search(query)
            Log.d("SearchViewModel", "New result value is ${searchResult.value}")
            _searchStatus.value = Status.SUCCESS
        } catch (e: Exception) {
            if (e !is CancellationException) {
                _searchStatus.value = Status.ERROR
                Log.e("SearchViewModel", "Hello from exception", e)
                searchResult.value = emptySet()
            }
        }
    }

    fun clearRecent() = viewModelScope.launch { searchRepository.clearRecentQueries() }

    fun getPopularStocks() = viewModelScope.launch {
        try {
            _popularStatus.value = Status.LOADING
            _popularRequests.value = placeholderList
            _popularRequests.value = searchRepository.getPopularStocks()
            _popularStatus.value = Status.SUCCESS
        } catch (e: Exception) {
            _popularStatus.value = Status.ERROR
            _popularRequests.value = placeholderList
        }
    }

    fun onStockClicked(stock: Stock) = viewModelScope.launch {
        searchRepository.insertQuery(stock)
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

@Suppress("UNCHECKED_CAST")
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
