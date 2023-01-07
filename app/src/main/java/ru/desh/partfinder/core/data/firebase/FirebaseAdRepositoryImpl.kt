package ru.desh.partfinder.core.data.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import ru.desh.partfinder.core.di.module.AdDbReference
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.search.AdsFilter
import ru.desh.partfinder.core.domain.model.search.Pagination
import ru.desh.partfinder.core.domain.repository.AdRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import javax.inject.Inject

class FirebaseAdRepositoryImpl @Inject constructor(
    @AdDbReference private val adDbReference: CollectionReference
): AdRepository {
    override fun getRecommendedAds(pagination: Pagination): LiveData<DataOrErrorResult<List<Ad>?, Exception?>> {
        val resultObserver = MutableLiveData<DataOrErrorResult<List<Ad>?, Exception?>>()
        val res = DataOrErrorResult<List<Ad>?, Exception?>()
        adDbReference.get().addOnSuccessListener { result ->
            val ads = mutableListOf<Ad>()
            for(document in result) {
                val ad = document.toObject(Ad::class.java)
                ads.add(ad)
            }
            res.data = ads
            resultObserver.value = res
        }.addOnFailureListener { exception ->
            res.exception = exception
            resultObserver.value = res
        }
        return resultObserver
    }

    override fun searchAds(
        adsFilter: AdsFilter,
        pagination: Pagination
    ): LiveData<DataOrErrorResult<List<Ad>?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun createAd(ad: Ad): LiveData<DataOrErrorResult<Boolean?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun deleteAd(adUid: String): LiveData<DataOrErrorResult<Boolean?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun storeAds(ads: List<Ad>) {
        TODO("Not yet implemented")
    }

    override fun getCachedAds(): LiveData<DataOrErrorResult<List<Ad>?, Exception?>> {
        TODO("Not yet implemented")
    }
}