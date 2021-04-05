package ru.maxultra.stonks.data.network

import com.squareup.moshi.Json
import ru.maxultra.stonks.data.database.DatabaseStock
import ru.maxultra.stonks.data.model.Stock

data class NetworkListStock(
    @Json(name = "symbol") val ticker: String,
    @Json(name = "name") val companyName: String?
)

fun List<NetworkListStock>.asDatabaseModel() =
    map {
        DatabaseStock(
            ticker = it.ticker,
            companyName = it.companyName
        )
    }

data class PopularListStock(
    @Json(name = "ticker") val ticker: String,
    @Json(name = "companyName") val companyName: String?
)

fun List<PopularListStock>.asDomainModel() =
    map {
        Stock(ticker = it.ticker, companyName = it.companyName ?: "")
    }

data class NetworkProfileStock(
    @Json(name = "symbol") val ticker: String,
    @Json(name = "companyName") val companyName: String?,
    @Json(name = "image") val logoUrl: String?,
    @Json(name = "currency") val currency: String?,
    @Json(name = "price") val currentStockPrice: Double?,
    @Json(name = "changes") val diff: Double?,
    @Json(name = "description") val description: String?,
    @Json(name = "exchange") val exchangeName: String?,
    @Json(name = "sector") val sector: String?,
    @Json(name = "website") val website: String?
)

fun NetworkProfileStock.asDatabaseModel() =
    DatabaseStock(
        ticker = ticker,
        companyName = companyName ?: "",
        logoUrl = logoUrl,
        currency = currency,
        currentPrice = currentStockPrice,
        dayChange = diff,
        description = description,
        exchangeName = exchangeName,
        sector = sector,
        website = website
    )

data class NetworkChartPoint(
    @Json(name = "date") val date: String,
    @Json(name = "close") val price: Double
)
