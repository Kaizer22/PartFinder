package ru.desh.partfinder.features.ads.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import ru.desh.partfinder.core.Screens.AdDetails
import ru.desh.partfinder.core.Screens.CategorySearch
import ru.desh.partfinder.core.Screens.NewsArticleSource
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.AdCategory
import ru.desh.partfinder.core.domain.model.BusinessArticle
import ru.desh.partfinder.core.domain.model.search.Pagination
import ru.desh.partfinder.core.domain.repository.AdRepository
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.domain.repository.BusinessNewsRepository
import javax.inject.Inject

class HomePageViewModel @Inject constructor(
    @AppNavigation private val router: Router,
    private val businessNewsRepository: BusinessNewsRepository,
    private val adRepository: AdRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var newsCurrentPage = 0
    private var recommendedAdsCurrentPage = 0

    val state: LiveData<HomePageState> get() = _state
    private val _state = MutableLiveData<HomePageState>()

    init {
        _state.value = HomePageState()
    }

    fun toAdCategorySearch(adCategory: AdCategory) = router.navigateTo(CategorySearch(adCategory))
    fun toAdDetails(ad: Ad) = router.navigateTo(AdDetails(ad))
    fun toBusinessArticleSource(url: String) = router.navigateTo(NewsArticleSource(url))


    fun displayName(): String =
        if (authRepository.getCurrentAccount()?.displayName.isNullOrEmpty()) "UserName" else
            authRepository.getCurrentAccount()?.displayName!!

    suspend fun requestRecommendedAdsNextPage() {
        val lastUid = if (_state.value?.recommendedAds?.size!! > 0)
            _state.value!!.recommendedAds.last().uid else null

        val result = adRepository.getRecommendedAds(
            Pagination.DEFAULT_PAGE_SIZE, ++recommendedAdsCurrentPage,
            lastUid
        )
        if (!result.isException) {
            val currentList = state.value?.recommendedAds.orEmpty()
            val newList = currentList + (result.data as List<Ad>).subtract(currentList)
            _state.value = state.value!!.copy(
                recommendedAds = newList
            )
        } else {
            //onFailureListener(exception)
        }
    }

    suspend fun requestBusinessNewsNextPage() {
        val result = businessNewsRepository.getLatestBusinessNews(
            Pagination.DEFAULT_PAGE_SIZE, ++newsCurrentPage
        )

        if (!result.isException) {
            val currentList = state.value?.businessArticles.orEmpty()
            val newList = currentList + result.data!!
            _state.value = state.value!!.copy(
                businessArticles = newList
            )
        } else {
            // onFailureListener(exception)
        }
    }
}

data class HomePageState(
    val isLoadingArticles: Boolean = true,
    val isLoadingAds: Boolean = true,
    val businessArticles: List<BusinessArticle> = emptyList(),
    val recommendedAds: List<Ad> = emptyList()
)
