package ru.desh.partfinder.features.ads.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.desh.partfinder.core.domain.model.AdCategory
import ru.desh.partfinder.databinding.ItemCategoryCardBinding

interface AdCategoriesActionListener {
    fun onCategory(adCategory: AdCategory)
}

// ListAdapter and DiffUtil are used in case of future dynamic categories list
class AdCategoriesAdapter(
    private val categoryActionListener: AdCategoriesActionListener
) : ListAdapter<Pair<AdCategory, Int>,
        AdCategoryViewHolder>(CategoryDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdCategoryViewHolder {
        val binding = ItemCategoryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return AdCategoryViewHolder(binding, categoryActionListener)
    }

    override fun onBindViewHolder(holder: AdCategoryViewHolder, position: Int) {
        holder.bind(currentList[position].first, currentList[position].second)
    }
}

class AdCategoryViewHolder(
    private val itemCategoryCardBinding: ItemCategoryCardBinding,
    private val adCategoriesActionListener: AdCategoriesActionListener
) : ViewHolder(itemCategoryCardBinding.root) {
    fun bind(category: AdCategory, @DrawableRes bg: Int) {
        itemView.setOnClickListener {
            adCategoriesActionListener.onCategory(category)
        }
        itemCategoryCardBinding.apply {
            itemCategoryHeader.text = category.name
            itemCategoryDescription.text = category.description
            itemCategoryBg.background = ResourcesCompat.getDrawable(
                itemView.resources,
                bg, null
            )
        }
    }
}

class CategoryDiffUtil : DiffUtil.ItemCallback<Pair<AdCategory, Int>>() {
    override fun areItemsTheSame(
        oldItem: Pair<AdCategory, Int>,
        newItem: Pair<AdCategory, Int>
    ): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: Pair<AdCategory, Int>,
        newItem: Pair<AdCategory, Int>
    ): Boolean =
        oldItem.first.name == newItem.first.name
}
