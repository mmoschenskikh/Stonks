package ru.maxultra.stonks.ui.stocklist

import androidx.recyclerview.widget.RecyclerView
import ru.maxultra.stonks.R
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.ItemStockBinding

class StockViewHolder(private val binding: ItemStockBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(stock: Stock, position: Int) {
        binding.tickerText.text = stock.ticker
        binding.nameText.text = stock.companyName
        setBackground(position)
    }

    private fun setBackground(position: Int) {
        val background =
            if (position % 2 == 0)
                R.drawable.bg_stock_dark
            else
                R.drawable.bg_stock_light
        binding.stockBackground.setBackgroundResource(background)
    }
}
