package ru.maxultra.stonks.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.maxultra.stonks.data.model.Stock
import java.util.*

/**
 * Represents a stock entity in the database.
 */
@Entity
data class DatabaseStock(
    @PrimaryKey val ticker: String,
    val companyName: String,
    val logoUrl: String? = null,
    val currency: String? = null,
    val currentPrice: Double? = null,
    val dayChange: Double? = null,
    val description: String? = null,
    val exchangeName: String? = null,
    val sector: String? = null,
    val website: String? = null,
    val priceMonth: String? = null,
    val priceYear: String? = null,
    val favourite: Boolean = false
)

fun List<DatabaseStock>.asDomainModel() =
    map {
        Stock(
            ticker = it.ticker,
            companyName = it.companyName,
            logoUrl = it.logoUrl,
            currency = it.currency?.run { Currency.getInstance(this) },
            currentStockPrice = it.currentPrice,
            dayChange = it.dayChange,
            favourite = it.favourite
        )
    }
