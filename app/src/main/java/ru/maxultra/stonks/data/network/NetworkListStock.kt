package ru.maxultra.stonks.data.network

import com.squareup.moshi.Json
import ru.maxultra.stonks.data.database.DatabaseStock

data class NetworkListStock(
    @Json(name = "symbol") val ticker: String,
    @Json(name = "name") val companyName: String
)

fun List<NetworkListStock>.asDatabaseModel() =
    map {
        DatabaseStock(
            ticker = it.ticker,
            companyName = it.companyName
        )
    }

