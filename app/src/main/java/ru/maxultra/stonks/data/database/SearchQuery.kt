package ru.maxultra.stonks.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.maxultra.stonks.data.model.Stock

@Entity
data class SearchQuery(
    @PrimaryKey val ticker: String,
    val companyName: String? = null
)

fun Stock.asRecentQuery() = SearchQuery(ticker = ticker, companyName = companyName)

fun List<SearchQuery>.asDomainModel() = map {
    Stock(
        ticker = it.ticker,
        companyName = it.companyName
    )
}
