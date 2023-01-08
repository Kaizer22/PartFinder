package ru.desh.partfinder.features.ads.data

import android.content.Context
import ru.desh.partfinder.R
import ru.desh.partfinder.core.domain.model.AdCategory

// Temporary solution for providing categories
object CategoryProvider {
    private const val CATEGORY_E_COMMERCE = "e_commerce"
    private const val CATEGORY_RETAIL = "retail"
    private const val CATEGORY_REAL_ESTATE = "real_estate"
    fun getCategories(context: Context): List<Pair<AdCategory, Int>> {
        context.apply {
            return listOf(
                Pair(AdCategory(
                    resources.getString(R.string.category_retail),
                    resources.getString(R.string.category_retail_description),
                    CATEGORY_RETAIL
                ), R.drawable.category_retail_placeholder),
                Pair(AdCategory(
                    resources.getString(R.string.category_e_commerce),
                    resources.getString(R.string.category_e_commerce_description),
                    CATEGORY_E_COMMERCE
                ), R.drawable.category_e_commerce_placeholder),
                Pair(AdCategory(
                    resources.getString(R.string.category_real_estate),
                    resources.getString(R.string.category_real_estate_description),
                    CATEGORY_REAL_ESTATE
                ), R.drawable.category_real_estate_placeholder)
            )
        }

    }
}