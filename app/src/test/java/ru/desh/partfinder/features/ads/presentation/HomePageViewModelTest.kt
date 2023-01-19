package ru.desh.partfinder.features.ads.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.BusinessArticle
import ru.desh.partfinder.core.domain.repository.AdRepository
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.domain.repository.BusinessNewsRepository
import ru.desh.partfinder.core.utils.DataOrErrorResult
import ru.desh.partfinder.core.utils.exception.UnknownNetworkErrorException
import java.util.*

@RunWith(AndroidJUnit4::class)
class HomePageViewModelTest {

    private lateinit var router: Router

    private lateinit var businessNewsRepository: BusinessNewsRepository

    private lateinit var adRepository: AdRepository

    private lateinit var authRepository: AuthRepository


    @Before
    fun before() {
        router = mock()
        businessNewsRepository = mock()

        adRepository = mock()
        authRepository = mock()
    }

    @Test
    fun testNewsApiDataFirstPageFetchSuccess() {
        // Given
        val pageSize = 3
        val pageNumber = 1
        val businessArticles = listOf(
            BusinessArticle(
                "source1", "author1", "title1", "description1", "url1", "imageUrl1", Date()
            ), BusinessArticle(
                "source2", "author2", "title2", "description2", "url2", "imageUrl2", Date()
            ), BusinessArticle(
                "source3", "author3", "title3", "description3", "url3", "imageUrl3", Date()
            )
        )
        val result = DataOrErrorResult<List<BusinessArticle>?, Exception?>(
            data = businessArticles, exception = null
        )

        val testBusinessNewsRepository: BusinessNewsRepository = mock {
            onBlocking {
                getLatestBusinessNews(pageSize, pageNumber)
            } doReturn result
        }
        val homePageViewModel = HomePageViewModel(
            router, testBusinessNewsRepository, adRepository, authRepository
        )

        // When
        runBlocking {
            homePageViewModel.requestBusinessNewsNextPage()
        }

        // Then
        Assertions.assertEquals(
            pageSize,
            homePageViewModel.homePageState.value?.businessArticles?.size ?: -1
        )
    }

    @Test
    fun testRecommendedAdsDataFirstPageFetchSuccess() {
        // Given
        val pageSize = 3
        val pageNumber = 1
        val lastAdUid = null // First page

        val result = DataOrErrorResult<List<Ad>?, Exception?>(
            listOf(Ad(), Ad(), Ad()), exception = null
        )

        val testAdRepository: AdRepository = mock {
            onBlocking {
                getRecommendedAds(pageSize, pageNumber, lastAdUid)
            } doReturn result
        }

        val homePageViewModel = HomePageViewModel(
            router, businessNewsRepository, testAdRepository, authRepository
        )

        // When
        runBlocking {
            homePageViewModel.requestRecommendedAdsNextPage()
        }

        // Then
        Assertions.assertEquals(pageSize, homePageViewModel.homePageState.value?.recommendedAds?.size ?: -1)
    }

    @Test
    fun testNewsApiDataFirstPageFetchFail() {
        // Given
        val pageSize = 3
        val pageNumber = 1
        val result = DataOrErrorResult<List<BusinessArticle>?, Exception?>(
            data = null, exception = UnknownNetworkErrorException("Expected exception")
        )

        val testBusinessNewsRepository: BusinessNewsRepository = mock {
            onBlocking {
                getLatestBusinessNews(pageSize, pageNumber)
            } doReturn result
        }
        val homePageViewModel = HomePageViewModel(
            router, testBusinessNewsRepository, adRepository, authRepository
        )

        // When
        runBlocking {
            homePageViewModel.requestBusinessNewsNextPage()
        }

        // Then
        Assertions.assertEquals(0, homePageViewModel.homePageState.value?.businessArticles?.size ?: -1)
    }

    @Test
    fun testRecommendedAdsDataFirstPageFetchFail() {
        // Given
        val pageSize = 3
        val pageNumber = 1
        val lastAdUid = null // First page
        val result = DataOrErrorResult<List<Ad>?, Exception?>(
            data = null, exception = Exception("Expected exception")
        )

        val testAdRepository: AdRepository = mock {
            onBlocking {
                getRecommendedAds(pageSize, pageNumber, lastAdUid)
            } doReturn result
        }

        val homePageViewModel = HomePageViewModel(
            router, businessNewsRepository, testAdRepository, authRepository
        )

        // When
        runBlocking {
            homePageViewModel.requestRecommendedAdsNextPage()
        }

        // Then
        Assertions.assertEquals(0, homePageViewModel.homePageState.value?.recommendedAds?.size ?: -1)
    }
}