package ru.maxultra.stonks.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.ItemWordBinding

class SuggestionListAdapter(private val onItemClicked: (Stock) -> Unit) :
    ListAdapter<Stock, SuggestionListViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Stock>() {
            override fun areItemsTheSame(oldItem: Stock, newItem: Stock) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Stock, newItem: Stock) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SuggestionListViewHolder(
            ItemWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SuggestionListViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClicked)
}
