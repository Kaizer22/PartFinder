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

    data class HomePageState(
        val isLoadingArticles: Boolean = true,
        val isLoadingAds: Boolean = true,
        val businessArticles: List<BusinessArticle> = emptyList(),
        val recommendedAds: List<Ad> = emptyList()
    )

    private val _homePageState = MutableLiveData(HomePageState())
    val homePageState: LiveData<HomePageState> = _homePageState


    fun toAdCategorySearch(adCategory: AdCategory) = router.navigateTo(CategorySearch(adCategory))
    fun toAdDetails(ad: Ad) = router.navigateTo(AdDetails(ad))
    fun toBusinessArticleSource(url: String) = router.navigateTo(NewsArticleSource(url))


    fun displayName(): String =
        if (authRepository.getCurrentAccount()?.displayName.isNullOrEmpty()) "UserName" else
            authRepository.getCurrentAccount()?.displayName!!

    suspend fun requestRecommendedAdsNextPage() {
        val lastUid = if (_homePageState.value?.recommendedAds?.size!! > 0)
            _homePageState.value!!.recommendedAds.last().uid else null

        val result = adRepository.getRecommendedAds(
            Pagination.DEFAULT_PAGE_SIZE, ++recommendedAdsCurrentPage,
            lastUid
        )
        if (!result.isException) {
            val currentList = homePageState.value?.recommendedAds.orEmpty()
            val newList = currentList + (result.data as List<Ad>).subtract(currentList)
            _homePageState.value = homePageState.value!!.copy(
                recommendedAds = newList
            )
        } else {
            // TODO failure handler
        }
    }

    suspend fun requestBusinessNewsNextPage() {
        val result = businessNewsRepository.getLatestBusinessNews(
            Pagination.DEFAULT_PAGE_SIZE, ++newsCurrentPage
        )

        if (!result.isException) {
            val currentList = homePageState.value?.businessArticles.orEmpty()
            val newList = currentList + result.data!!
            _homePageState.value = homePageState.value!!.copy(
                businessArticles = newList
            )
        } else {
            // TODO failure handler
        }
    }
}


