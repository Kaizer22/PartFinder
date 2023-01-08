package ru.desh.partfinder.features.ads.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import ru.desh.partfinder.R
import ru.desh.partfinder.core.Screens.AdDetails
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.core.utils.DateHelper
import ru.desh.partfinder.databinding.ItemAdCardBinding
import java.util.Date

class AdsAdapter(
    private val router: Router
) : ListAdapter<Ad, AdViewHolder>
    (AdDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAdCardBinding.inflate(layoutInflater, parent, false)
        return AdViewHolder(binding, router, layoutInflater)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class AdViewHolder(
    private val itemAdCardBinding: ItemAdCardBinding,
    private val router: Router,
    layoutInflater: LayoutInflater
    ): ViewHolder(itemAdCardBinding.root) {

    private val todoMessage: SnackbarBuilder = SnackbarBuilder(
        itemAdCardBinding.root,
        layoutInflater, Snackbar.LENGTH_LONG
    )
        .setType(SnackbarBuilder.Type.SECONDARY)
        .setTitle(itemView.context.getString(R.string.message_title_todo))
        .setText(itemView.context.getString(R.string.message_text_todo))

    // TODO check those in a proper way, calling cache and api
    private var isInFavourites = false
    private var isLiked = false
    private var isDisliked = false

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
            adCardDate.text = DateHelper.dateToText(Date(
                ad.creationTimestamp * 1000))
            adCardRating.text = ad.reputation.toString()
            adCardCommentsCounter.text = ad.commentsCount.toString()
            adCardFavouriteCounter.text = ad.favouritesCount.toString()

            adCardButtonAddFavourite.setOnClickListener {
                isInFavourites = !isInFavourites
                setFavouritesButtonSrc()
                todoMessage.show()
            }
            adCardButtonLike.setOnClickListener {
                ad.reputation = ad.reputation + if (isLiked && !isDisliked) -1
                else if (!isLiked && isDisliked) 2
                else if (isLiked) -1
                else 1
                adCardRating.text = ad.reputation.toString()
                isLiked = !isLiked
                isDisliked = false
                setReactionButtonsSrc()
                todoMessage.show()
            }
            adCardButtonDislike.setOnClickListener {
                ad.reputation = ad.reputation + if (!isLiked && isDisliked) 1
                else if (isLiked && !isDisliked) -2
                else if (isDisliked) 1
                else -1
                adCardRating.text = ad.reputation.toString()
                isLiked = false
                isDisliked = !isDisliked
                setReactionButtonsSrc()
                todoMessage.show()
            }
            setFavouritesButtonSrc()
            setReactionButtonsSrc()
        }
    }

    private fun setFavouritesButtonSrc() {
        itemAdCardBinding.apply {
            adCardButtonAddFavourite.setImageDrawable(
                if (isInFavourites)
                    ResourcesCompat.getDrawable(
                        itemView.resources,
                        R.drawable.ic_star_outer_shadow, null
                    )
                else ResourcesCompat.getDrawable(
                    itemView.resources,
                    R.drawable.ic_star_inner_shadow, null
                )
            )
        }
    }

    private fun setReactionButtonsSrc() {
        itemAdCardBinding.apply {
            adCardButtonLike.isSelected = isLiked && !isDisliked
        }
        itemAdCardBinding.apply {
            adCardButtonDislike.isSelected = !isLiked && isDisliked
        }
    }
}


class AdDiffUtilCallback: DiffUtil.ItemCallback<Ad>() {
    override fun areItemsTheSame(oldItem: Ad, newItem: Ad): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: Ad, newItem: Ad): Boolean =
        oldItem.uid == newItem.uid
}
