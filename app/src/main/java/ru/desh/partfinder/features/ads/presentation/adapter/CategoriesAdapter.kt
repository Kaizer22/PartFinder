package ru.desh.partfinder.features.ads.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.desh.partfinder.core.domain.model.AdCategory
import ru.desh.partfinder.databinding.ItemCategoryCardBinding

// ListAdapter and DiffUtil are used in case of future dynamic categories list
class CategoriesAdapter: ListAdapter<AdCategory,
        CategoryViewHolder>(CategoryDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class CategoryViewHolder(
    private val itemCategoryCardBinding: ItemCategoryCardBinding
): ViewHolder(itemCategoryCardBinding.root) {
    fun bind(category: AdCategory) {
        itemCategoryCardBinding.apply {
            itemCategoryHeader.text = category.name
        }
        itemCategoryCardBinding.apply {
            itemCategoryDescription.text = category.description
        }
    }
}

class CategoryDiffUtil: DiffUtil.ItemCallback<AdCategory>(){
    override fun areItemsTheSame(oldItem: AdCategory, newItem: AdCategory): Boolean =
        oldItem == newItem
    override fun areContentsTheSame(oldItem: AdCategory, newItem: AdCategory): Boolean =
        oldItem.name == newItem.name
}
