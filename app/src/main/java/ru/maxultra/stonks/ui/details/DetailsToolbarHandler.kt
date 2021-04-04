package ru.maxultra.stonks.ui.details

import ru.maxultra.stonks.data.model.StockDetails

interface DetailsToolbarHandler {
    fun setDetailsToolbarFields(stock: StockDetails)
}
