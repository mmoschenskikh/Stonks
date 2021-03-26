package ru.maxultra.stonks.ui.stocklist

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.maxultra.stonks.R
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.ItemStockBinding
import ru.maxultra.stonks.ui.formatPrice
import ru.maxultra.stonks.ui.formatPriceDelta
import ru.maxultra.stonks.ui.priceDeltaColor

class StockViewHolder(private val binding: ItemStockBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        stock: Stock,
        position: Int,
        onItemClicked: (Stock) -> Unit,
        onFavouriteClicked: (Stock) -> Unit
    ) {
        binding.tickerText.text = stock.ticker
        binding.nameText.text = stock.companyName
        setBackground(position)
        binding.root.setOnClickListener { onItemClicked(stock) }
        binding.starImage.setImageResource(getStarImage(stock.favourite))
        binding.starImage.setOnClickListener {
            onFavouriteClicked(stock)
            binding.starImage.setImageResource(getStarImage(stock.favourite))
        }
        binding.stockImage.clipToOutline = true
        binding.stockImage.load(stock.logoUrl)
        stock.currency?.let { currency ->
            stock.currentStockPrice?.let { price ->
                binding.price.text = formatPrice(currency, price)
                stock.dayChange?.let { diff ->
                    binding.priceDeltaText.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            priceDeltaColor(diff)
                        )
                    )
                    binding.priceDeltaText.text = formatPriceDelta(currency, price, diff)
                }
            }
        }
    }

    private fun getStarImage(favourite: Boolean) =
        if (favourite) R.drawable.star_yellow else R.drawable.star_grey

    private fun setBackground(position: Int) {
        val background =
            if (position % 2 == 0)
                R.drawable.bg_stock_dark
            else
                R.drawable.bg_stock_light
        binding.stockBackground.setBackgroundResource(background)
    }
}
