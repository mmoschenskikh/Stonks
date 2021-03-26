package ru.maxultra.stonks.data.repository

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
        val newStockList = fmpService.getStocks().asDatabaseModel()
        stockDao.insertAll(newStockList)
        newStockList.forEach { // TODO: Load multiple profiles at the time (probably coupled with paging)
            val stockProfile = fmpService.getProfile(it.ticker)[0]
            val oldDbStock = stockDao.getStock(stockProfile.ticker)
            val newDbStock = oldDbStock.update(
                companyName = stockProfile.companyName,
                logoUrl = stockProfile.logoUrl,
                currency = stockProfile.currency,
                currentPrice = stockProfile.currentStockPrice,
                dayChange = stockProfile.diff,
                description = stockProfile.description,
                exchangeName = stockProfile.exchangeName,
                sector = stockProfile.sector,
                website = stockProfile.website
            )
            stockDao.update(newDbStock)
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
}
