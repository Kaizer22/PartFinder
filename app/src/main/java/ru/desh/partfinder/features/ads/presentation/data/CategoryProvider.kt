package ru.desh.partfinder.features.ads.presentation.data

import android.content.Context
import ru.desh.partfinder.R
import ru.desh.partfinder.core.domain.model.AdCategory

object CategoryProvider {
    const val CATEGORY_E_COMMERCE = "e_commerce"
    const val CATEGORY_RETAIL = "retail"
    const val CATEGORY_REAL_ESTATE = "real_estate"
    fun getCategories(context: Context): List<AdCategory> {
        context.apply {
            return listOf(
                AdCategory(
                    resources.getString(R.string.category_retail),
                    resources.getString(R.string.category_retail_description),
                    CATEGORY_RETAIL
                ),
                AdCategory(
                    resources.getString(R.string.category_e_commerce),
                    resources.getString(R.string.category_e_commerce_description),
                    CATEGORY_E_COMMERCE
                ),
                AdCategory(
                    resources.getString(R.string.category_real_estate),
                    resources.getString(R.string.category_real_estate_description),
                    CATEGORY_REAL_ESTATE
                )
            )
        }

    }
}