package ru.desh.partfinder.core.domain.repository

import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.search.AdsFilter
import ru.desh.partfinder.core.domain.model.search.AdsPagination
import ru.desh.partfinder.core.utils.DataOrErrorResult

interface AdRepository {
    // Remote
    suspend fun getRecommendedAds(
        pageSize: Int,
        pageNumber: Int,
        lastAdUid: String?
    ): DataOrErrorResult<List<Ad>?, Exception?>

    suspend fun searchAds(
        adsFilter: AdsFilter,
        pagination: AdsPagination
    ): DataOrErrorResult<List<Ad>?, Exception?>

    suspend fun createAd(ad: Ad): DataOrErrorResult<Boolean, Exception?>

    suspend fun deleteAd(adUid: String): DataOrErrorResult<Boolean, Exception?>

    // Cache
    fun storeAds(ads: List<Ad>)
    suspend fun getCachedAds(): DataOrErrorResult<List<Ad>?, Exception?>
}