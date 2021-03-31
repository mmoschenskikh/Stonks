package ru.maxultra.stonks.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
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
    fun getStocks(): LiveData<List<Stock>> = liveData {
        val source = Transformations.map(stockDao.getStocks()) { it.asDomainModel() }
        emitSource(source)
        try {
            val newStockList = fmpService.getStocks().asDatabaseModel()
            stockDao.insertAll(newStockList)
            newStockList.chunked(50).forEach { chunkedPart ->
                val profilesQuery = chunkedPart.joinToString { it.ticker }
                val profilesResponse = fmpService.getProfile(profilesQuery)
                profilesResponse.forEach {
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
                }
            }
            source.value?.let { stockList ->
                val requestedTickers = newStockList.map { it.ticker }
                stockList.filterNot { it.ticker in requestedTickers }.chunked(50)
                    .forEach { chunkedPart ->
                        val existingQuery =
                            chunkedPart.joinToString { it.ticker }
                        val existingResponse = fmpService.getProfile(existingQuery)
                        existingResponse.forEach {
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
                        }
                    }
            }
        } catch (e: Exception) {
            Log.e("StockRepository", e.message ?: " ")
        }

    }

    fun getFavouriteStocks(): LiveData<List<Stock>> = liveData {
        val source = Transformations.map(stockDao.getFavouriteStocks()) { it.asDomainModel() }
        emitSource(source)
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

    suspend fun getPopularStocks(): List<Stock> =
        fmpService.getPopular()
            .filterNot { it.companyName.isNullOrBlank() }
            .sortedBy { it.companyName!!.length }
            .asDomainModel()
}
