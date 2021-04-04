package ru.maxultra.stonks.ui.search.suggestionlist

import androidx.recyclerview.widget.RecyclerView
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.ItemWordBinding

class SuggestionListViewHolder(private val binding: ItemWordBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(stock: Stock, onItemClicked: (Stock) -> Unit) {
        binding.wordText.text = stock.companyName
        binding.root.setOnClickListener { onItemClicked(stock) }
    }
}
