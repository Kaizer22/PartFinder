package ru.desh.partfinder.features.ads.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import ru.desh.partfinder.core.domain.model.BusinessArticle
import ru.desh.partfinder.core.utils.DateHelper
import ru.desh.partfinder.databinding.ItemBusinessNewsCardBinding
import java.util.Locale

interface BusinessArticlesActionListener {
    fun onBusinessArticleSource(url: String)
}

class BusinessArticleAdapter(
    private val businessArticlesActionListener: BusinessArticlesActionListener
) : ListAdapter<BusinessArticle,
        BusinessArticleViewHolder>(BusinessArticleDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessArticleViewHolder {
        val binding = ItemBusinessNewsCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return BusinessArticleViewHolder(binding, businessArticlesActionListener)
    }

    override fun onBindViewHolder(holder: BusinessArticleViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class BusinessArticleViewHolder(
    private val itemBusinessNewsCardBinding: ItemBusinessNewsCardBinding,
    private val businessArticlesActionListener: BusinessArticlesActionListener
) :
    ViewHolder(itemBusinessNewsCardBinding.root) {
    fun bind(businessArticle: BusinessArticle) {
        itemBusinessNewsCardBinding.apply {
            itemView.setOnClickListener {
                businessArticlesActionListener.onBusinessArticleSource(businessArticle.url)
                //router.navigateTo(NewsArticleSource(businessArticle.url))
            }
            Glide.with(itemView)
                .load(businessArticle.urlToImage)
                .into(itemBusinessNewsImage)
            itemBusinessNewsTitle.text = businessArticle.title
            itemBusinessNewsDate.text =
                DateHelper.dateToText(businessArticle.publishedAt, Locale.getDefault())
        }
    }
}

class BusinessArticleDiffUtilCallback : DiffUtil.ItemCallback<BusinessArticle>() {
    override fun areItemsTheSame(oldItem: BusinessArticle, newItem: BusinessArticle): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: BusinessArticle, newItem: BusinessArticle): Boolean =
        oldItem.title == newItem.title
}
