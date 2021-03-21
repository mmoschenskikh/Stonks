package ru.maxultra.stonks.ui.search

import androidx.recyclerview.widget.RecyclerView
import ru.maxultra.stonks.databinding.ItemWordBinding

class SuggestionListViewHolder(private val binding: ItemWordBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(word: String, onItemClicked: (String) -> Unit) {
        binding.wordText.text = word
        binding.root.setOnClickListener { onItemClicked(word) }
    }
}
