package ru.desh.partfinder.core.data.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import ru.desh.partfinder.core.di.module.AdDbReference
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.search.AdsFilter
import ru.desh.partfinder.core.domain.model.search.AdsPagination
import ru.desh.partfinder.core.domain.repository.AdRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import javax.inject.Inject

class FirebaseAdRepositoryImpl @Inject constructor(
    @AdDbReference private val adDbReference: CollectionReference
): AdRepository {
    companion object {
        const val ORDER_BY_FIELD = "creationTimestamp"
        const val UID_FIELD = "uid"
    }
    override fun getRecommendedAds(pagination: AdsPagination): LiveData<DataOrErrorResult<List<Ad>?, Exception?>> {
        val resultObserver = MutableLiveData<DataOrErrorResult<List<Ad>?, Exception?>>()
        val res = DataOrErrorResult<List<Ad>?, Exception?>()
        // If there is no previous page just return first N
        // else get the DocumentReference of last Ad and get ads starting from it with limit
        // https://firebase.google.com/docs/firestore/query-data/query-cursors?hl=en&skip_cache=true%22#kotlin+ktx_3
        if (pagination.lastUid.isNullOrEmpty()) {
            adDbReference.orderBy(ORDER_BY_FIELD)
                .limit(pagination.pageSize.toLong())
                .get().addOnSuccessListener { result ->
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
        } else {
            adDbReference.whereEqualTo(UID_FIELD, pagination.lastUid).get().addOnSuccessListener {
                adDbReference.orderBy(ORDER_BY_FIELD)
                    .startAfter(it.documents[0])
                    .limit(pagination.pageSize.toLong()).get().addOnSuccessListener { result ->
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
            }
        }
        return resultObserver
    }

    override fun searchAds(
        adsFilter: AdsFilter,
        pagination: AdsPagination
    ): LiveData<DataOrErrorResult<List<Ad>?, Exception?>> {
        TODO("Not yet implemented")
    }

    override fun createAd(ad: Ad): LiveData<DataOrErrorResult<Boolean?, Exception?>> {
        val resultObserver = MutableLiveData<DataOrErrorResult<Boolean?, Exception?>>()
        adDbReference.document(ad.uid).set(ad).addOnSuccessListener {
            resultObserver.value = DataOrErrorResult(true, null)
        }.addOnFailureListener {
            resultObserver.value = DataOrErrorResult(false, it)
        }
        return resultObserver
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