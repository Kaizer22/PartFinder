package ru.desh.partfinder.features.ads.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.terrakok.cicerone.Router
import ru.desh.partfinder.core.Screens.CategorySearch
import ru.desh.partfinder.core.domain.model.AdCategory
import ru.desh.partfinder.databinding.ItemCategoryCardBinding

// ListAdapter and DiffUtil are used in case of future dynamic categories list
class CategoriesAdapter(
    private val router: Router
): ListAdapter<Pair<AdCategory, Int>,
        CategoryViewHolder>(CategoryDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CategoryViewHolder(binding, router)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(currentList[position].first, currentList[position].second)
    }
}

class CategoryViewHolder(
    private val itemCategoryCardBinding: ItemCategoryCardBinding,
    private val router: Router
): ViewHolder(itemCategoryCardBinding.root) {
    fun bind(category: AdCategory, @DrawableRes bg:  Int) {
        itemView.setOnClickListener {
            router.navigateTo(CategorySearch(category.name))
        }
        itemCategoryCardBinding.apply {
            itemCategoryHeader.text = category.name
            itemCategoryDescription.text = category.description
            itemCategoryBg.background = ResourcesCompat.getDrawable(itemView.resources,
            bg, null)
        }
    }
}

class CategoryDiffUtil: DiffUtil.ItemCallback<Pair<AdCategory, Int>>(){
    override fun areItemsTheSame(oldItem: Pair<AdCategory, Int>, newItem: Pair<AdCategory, Int>): Boolean =
        oldItem == newItem
    override fun areContentsTheSame(oldItem: Pair<AdCategory, Int>, newItem: Pair<AdCategory, Int>): Boolean =
        oldItem.first.name == newItem.first.name
}
