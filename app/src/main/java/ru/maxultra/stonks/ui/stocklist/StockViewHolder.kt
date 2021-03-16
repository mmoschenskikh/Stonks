package ru.maxultra.stonks.ui.stocklist

import androidx.recyclerview.widget.RecyclerView
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.ItemStockBinding

class StockViewHolder(private val binding: ItemStockBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(stock: Stock) {
        binding.tickerText.text = stock.ticker
        binding.nameText.text = stock.companyName
    }
}
