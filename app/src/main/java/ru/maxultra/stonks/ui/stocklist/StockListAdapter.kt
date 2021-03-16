package ru.maxultra.stonks.ui.stocklist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.maxultra.stonks.data.model.Stock

class StockListAdapter : ListAdapter<Stock, StockViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Stock>() {
            override fun areItemsTheSame(oldItem: Stock, newItem: Stock): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(oldItem: Stock, newItem: Stock): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}
