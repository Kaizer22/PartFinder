package ru.desh.partfinder.core.data.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import ru.desh.partfinder.core.di.module.AdDbReference
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.search.AdsFilter
import ru.desh.partfinder.core.domain.model.search.AdsPagination
import ru.desh.partfinder.core.domain.repository.AdRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import javax.inject.Inject

class FirebaseAdRepositoryImpl @Inject constructor(
    @AdDbReference private val adDbReference: CollectionReference
) : AdRepository {
    companion object {
        const val ORDER_BY_FIELD = "creationTimestamp"
        const val UID_FIELD = "uid"
    }

    override suspend fun getRecommendedAds(
        pageSize: Int,
        pageNumber: Int,
        lastAdUid: String?
    ): DataOrErrorResult<List<Ad>?, Exception?> {
        val res = DataOrErrorResult<List<Ad>?, Exception?>()
        // If there is no previous page just return first N
        // else get the DocumentReference of last Ad and get ads starting from it with limit
        // https://firebase.google.com/docs/firestore/query-data/query-cursors?hl=en&skip_cache=true%22#kotlin+ktx_3
        if (lastAdUid.isNullOrEmpty()) {
            try {
                val result = adDbReference.orderBy(ORDER_BY_FIELD)
                    .limit(pageSize.toLong())
                    .get().await()
                val ads = mutableListOf<Ad>()
                for (document in result) {
                    val ad = document.toObject(Ad::class.java)
                    ads.add(ad)
                }
                res.setData(ads)
            } catch (e: Exception) {
                res.setException(e)
            }
        } else {
            try {
                val lastDocResult =
                    adDbReference.whereEqualTo(UID_FIELD, lastAdUid).get().await()
                val result = adDbReference.orderBy(ORDER_BY_FIELD)
                    .startAfter(lastDocResult.documents[0])
                    .limit(pageSize.toLong()).get().await()
                val ads = mutableListOf<Ad>()
                for (document in result) {
                    val ad = document.toObject(Ad::class.java)
                    ads.add(ad)
                }
                res.setData(ads)
            } catch (e: Exception) {
                res.setException(e)
            }
        }
        return res
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