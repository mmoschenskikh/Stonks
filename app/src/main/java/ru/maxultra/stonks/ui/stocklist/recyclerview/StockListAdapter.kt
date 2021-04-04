package ru.maxultra.stonks.ui.stocklist.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.ItemStockBinding

class StockListAdapter(
    private val onItemClicked: (Stock) -> Unit,
    private val onFavouriteClicked: (Stock) -> Unit
) :
    ListAdapter<Stock, StockViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Stock>() {
            override fun areItemsTheSame(oldItem: Stock, newItem: Stock) =
                oldItem.ticker == newItem.ticker

            override fun areContentsTheSame(oldItem: Stock, newItem: Stock) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        StockViewHolder(
            ItemStockBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) =
        holder.bind(getItem(position), position, onItemClicked, onFavouriteClicked)
}
