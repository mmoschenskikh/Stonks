package ru.maxultra.stonks.data.repository

import kotlinx.coroutines.flow.map
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
    fun getStocks() = stockDao.getStocks().map { it.asDomainModel() }

    fun getFavouriteStocks() = stockDao.getFavouriteStocks().map { it.asDomainModel() }

    suspend fun getStockList() {
        val networkStockList = fmpService.getStocks()
            .filter { it.ticker.length < 13 && it.companyName != null }
        stockDao.insertAll(networkStockList.asDatabaseModel())
        updateProfiles(networkStockList.map { it.ticker })
    }

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
