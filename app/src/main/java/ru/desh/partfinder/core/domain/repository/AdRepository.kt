package ru.desh.partfinder.core.domain.repository

import androidx.lifecycle.LiveData
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.search.AdsFilter
import ru.desh.partfinder.core.domain.model.search.AdsPagination
import ru.desh.partfinder.core.utils.DataOrErrorResult

interface AdRepository {
    // Remote
    suspend fun getRecommendedAds(pageSize: Int, pageNumber: Int, lastAdUid: String?): DataOrErrorResult<List<Ad>?, Exception?>
    fun searchAds(
        adsFilter: AdsFilter,
        pagination: AdsPagination
    ): LiveData<DataOrErrorResult<List<Ad>?, Exception?>>

    fun createAd(ad: Ad): LiveData<DataOrErrorResult<
            Boolean?, Exception?>>

    fun deleteAd(adUid: String): LiveData<DataOrErrorResult<
            Boolean?, Exception?>>

    // Cache
    fun storeAds(ads: List<Ad>)
    fun getCachedAds(): LiveData<DataOrErrorResult<
            List<Ad>?, Exception?>>
}