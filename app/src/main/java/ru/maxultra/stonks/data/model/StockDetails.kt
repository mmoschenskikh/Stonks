package ru.maxultra.stonks.data.model

import java.util.*

data class StockDetails(
    val ticker: String,
    val companyName: String,
    var currency: Currency? = null,
    var currentStockPrice: Double? = null,
    var dayChange: Double? = null,
    var favourite: Boolean = false,
    val description: String? = null,
    val exchangeName: String? = null,
    val sector: String? = null,
    val website: String? = null,
    val priceDay: String? = null,
    val priceMonth: String? = null,
    val priceYear: String? = null,
)
