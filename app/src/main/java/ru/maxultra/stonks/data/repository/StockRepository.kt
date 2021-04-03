package ru.maxultra.stonks.data.repository

import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import ru.maxultra.stonks.data.database.StockDao
import ru.maxultra.stonks.data.database.asDomainModel
import ru.maxultra.stonks.data.database.update
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.data.network.FmpService
import ru.maxultra.stonks.data.network.asDatabaseModel

class StockRepository(
    private val stockDao: StockDao,
    private val fmpService: FmpService
) {
    /**
     * Provides LiveData containing all the stocks from the database.
     */
    fun getStocks() = liveData<List<Stock>> {
        val source = Transformations.map(stockDao.getStocks()) { it.asDomainModel() }
        emitSource(source)
    }

    /**
     * Provides LiveData containing only favourite stocks.
     */
    fun getFavouriteStocks() = liveData<List<Stock>> {
        val source = Transformations.map(stockDao.getFavouriteStocks()) { it.asDomainModel() }
        emitSource(source)
    }

    /**
     * Toggles the "favourite" state.
     */
    suspend fun toggleFavourite(stock: Stock) {
        val dbStock = stockDao.getStock(stock.ticker)
        val newDbStock = dbStock.update(favourite = !dbStock.favourite)
        stockDao.update(newDbStock)
    }

    /**
     * Fetches the stock list from the remote API and caches it in the database.
     * Note that the method only gets tickers and company names, so [updateProfiles] should be called after getting the list.
     * Some stocks may be discarded (considered as invalid) to provide better UX.
     */
    suspend fun getStockList() {
        val networkStockList = fmpService.getStocks()
            .filter { it.ticker.length < 13 && it.companyName != null }
        stockDao.insertAll(networkStockList.asDatabaseModel())
    }

    /**
     * Fetches stock profiles for [tickerList] from the remote API and updates database entries.
     * Some stocks may be discarded (considered as invalid) to provide better UX.
     */
    suspend fun updateProfiles(tickerList: List<String>) {
        tickerList.chunked(50).forEach { chunk ->
            val query = chunk.joinToString()
            val response = fmpService.getProfile(query)
            response.forEach {
                if (it.ticker.length < 13
                    && it.companyName != null
                    && it.currentStockPrice != null
                    && it.currentStockPrice != 0.0
                    && it.currentStockPrice < 1000000
                    && it.diff != null
                    && it.diff.isFinite()
                ) {
                    val oldDbStock = stockDao.getStock(it.ticker)
                    val newDbStock = oldDbStock.update(
                        companyName = it.companyName,
                        logoUrl = it.logoUrl,
                        currency = it.currency,
                        currentPrice = it.currentStockPrice,
                        dayChange = it.diff,
                        description = it.description,
                        exchangeName = it.exchangeName,
                        sector = it.sector,
                        website = it.website
                    )
                    stockDao.update(newDbStock)
                } else {
                    stockDao.removeStock(it.ticker)
                }
            }
        }
    }


    suspend fun toggleFavourite(stock: Stock) {
        val dbStock = stockDao.getStock(stock.ticker)
        val newDbStock = dbStock.update(favourite = !dbStock.favourite)
        stockDao.update(newDbStock)
    }

    suspend fun search(query: String): List<Stock> {
        try {
            val searchResultList = fmpService.search(query).asDatabaseModel()
            stockDao.insertAll(searchResultList)
            val searchResultProfiles = searchResultList.joinToString { it.ticker }
            val searchResultProfilesResponse = fmpService.getProfile(searchResultProfiles)
            val loadedSearchResultList = searchResultProfilesResponse.map {
                val oldDbStock = stockDao.getStock(it.ticker)
                val newDbStock = oldDbStock.update(
                    companyName = it.companyName,
                    logoUrl = it.logoUrl,
                    currency = it.currency,
                    currentPrice = it.currentStockPrice,
                    dayChange = it.diff,
                    description = it.description,
                    exchangeName = it.exchangeName,
                    sector = it.sector,
                    website = it.website
                )
                stockDao.update(newDbStock)
                newDbStock
            }
            return loadedSearchResultList.asDomainModel()
        } catch (e: Exception) {
            Log.e("StockRepository", e.message ?: " ")
        }
        return emptyList()
    }
}
