package ru.desh.partfinder.core.domain.repository

import androidx.lifecycle.LiveData
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.search.AdsFilter
import ru.desh.partfinder.core.domain.model.search.Pagination
import ru.desh.partfinder.core.utils.DataOrErrorResult

interface AdRepository {
    // Remote
    fun getRecommendedAds(pagination: Pagination):LiveData<DataOrErrorResult<List<Ad>?, Exception?>>
    fun searchAds(adsFilter: AdsFilter, pagination: Pagination):LiveData<DataOrErrorResult<List<Ad>?, Exception?>>

    fun createAd(ad: Ad): LiveData<DataOrErrorResult<
            Boolean?, Exception?>>
    fun deleteAd(adUid: String):LiveData<DataOrErrorResult<
            Boolean?, Exception?>>

    // Cache
    fun storeAds(ads: List<Ad>)
    fun getCachedAds(): LiveData<DataOrErrorResult<
            List<Ad>?, Exception?>>
}