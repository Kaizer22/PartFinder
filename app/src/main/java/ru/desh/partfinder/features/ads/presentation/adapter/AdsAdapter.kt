package ru.desh.partfinder.features.ads.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.terrakok.cicerone.Router
import ru.desh.partfinder.core.Screens.AdDetails
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.utils.DateHelper
import ru.desh.partfinder.databinding.ItemAdCardBinding
import java.util.Date

class AdsAdapter(
    private val router: Router
) : ListAdapter<Ad, AdViewHolder>
    (AdDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val binding = ItemAdCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return AdViewHolder(binding, router)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class AdViewHolder(
    private val itemAdCardBinding: ItemAdCardBinding,
    private val router: Router
    ): ViewHolder(itemAdCardBinding.root) {

        fun bind(ad: Ad) {
            itemView.setOnClickListener {
                router.navigateTo(AdDetails(ad))
            }
            itemAdCardBinding.apply {
//                if (ad.media.isNotEmpty()) {
//                    Glide.with(itemView.context)
//                        .load(ad.media[0].link)
//                        .into(adCardImage)
//                }
                adCardTitle.text = ad.title
                adCardTarget.text = ad.target
                adCardDate.text = DateHelper.dateToText(Date(ad.creationTimestamp))
                adCardRating.text = ad.reputation.toString()
                adCardCommentsCounter.text = ad.commentsCount.toString()
                adCardFavouriteCounter.text = ad.favouritesCount.toString()
            }
        }
}

class AdDiffUtilCallback: DiffUtil.ItemCallback<Ad>() {
    override fun areItemsTheSame(oldItem: Ad, newItem: Ad): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: Ad, newItem: Ad): Boolean =
        oldItem.uid == newItem.uid
}
