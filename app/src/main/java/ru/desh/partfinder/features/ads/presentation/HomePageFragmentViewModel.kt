package ru.desh.partfinder.features.ads.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.BusinessArticle
import ru.desh.partfinder.core.domain.model.search.Pagination
import ru.desh.partfinder.core.domain.repository.AdRepository
import ru.desh.partfinder.core.domain.repository.BusinessNewsRepository
import javax.inject.Inject

class HomePageFragmentViewModel @Inject constructor(
    private val businessNewsRepository: BusinessNewsRepository,
    private val adRepository: AdRepository,
): ViewModel() {

    private var newsCurrentPage = 0
    private var recommendedAdsCurrentPage = 0

    val state: LiveData<HomePageState> get() = _state
    private val _state = MutableLiveData<HomePageState>()

    init{
        _state.value = HomePageState()
    }

    fun requestRecommendedAdsNextPage(owner: LifecycleOwner,
        onFailureListener: (Exception) -> Unit = {}){
        adRepository.getRecommendedAds(
            Pagination(
                Pagination.DEFAULT_PAGE_SIZE, ++recommendedAdsCurrentPage
            )
        ).observe(owner) { result ->
            if (!result.isException) {
                val currentList = state.value?.recommendedAds.orEmpty()
                val newList = currentList + (result.data as List<Ad>)
                _state.value = state.value!!.copy(
                    recommendedAds = newList
                )
            } else {
                onFailureListener(result.exception!!)
            }
        }
    }

    fun requestBusinessNewsNextPage(){
        viewModelScope.launch {
            val articles = businessNewsRepository.getLatestBusinessNews(
                Pagination(Pagination.DEFAULT_PAGE_SIZE, ++newsCurrentPage)
            )
            val currentList = state.value?.businessArticles.orEmpty()
            val newList = currentList + articles
            _state.value = state.value!!.copy(
                businessArticles = newList
            )
        }
    }
}

data class HomePageState(
    val isLoadingArticles: Boolean = true,
    val isLoadingAds: Boolean = true,
    val businessArticles: List<BusinessArticle> = emptyList(),
    val recommendedAds: List<Ad> = emptyList()
)