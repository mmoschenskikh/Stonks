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
    val companyName: String? = null,
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
        val currency = try {
            it.currency?.run { Currency.getInstance(this) }
        } catch (e: Exception) { // Default currency is USD
            Currency.getInstance("USD")
        }
        Stock(
            ticker = it.ticker,
            companyName = it.companyName,
            logoUrl = it.logoUrl,
            currency = currency,
            currentStockPrice = it.currentPrice,
            dayChange = it.dayChange,
            favourite = it.favourite
        )
    }

/**
 * Updates some fields without losing existing.
 */
fun DatabaseStock.update(
    companyName: String? = null,
    logoUrl: String? = null,
    currency: String? = null,
    currentPrice: Double? = null,
    dayChange: Double? = null,
    description: String? = null,
    exchangeName: String? = null,
    sector: String? = null,
    website: String? = null,
    priceMonth: String? = null,
    priceYear: String? = null,
    favourite: Boolean? = null
) = DatabaseStock(
    ticker = this.ticker,
    companyName = companyName ?: this.companyName,
    logoUrl = logoUrl ?: this.logoUrl,
    currency = currency ?: this.currency,
    currentPrice = currentPrice ?: this.currentPrice,
    dayChange = dayChange ?: this.dayChange,
    description = description ?: this.description,
    exchangeName = exchangeName ?: this.exchangeName,
    sector = sector ?: this.sector,
    website = website ?: this.website,
    priceMonth = priceMonth ?: this.priceMonth,
    priceYear = priceYear ?: this.priceYear,
    favourite = favourite ?: this.favourite
)

