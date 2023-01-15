package ru.desh.partfinder.features.ads.presentation

import com.github.terrakok.cicerone.Router
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import ru.desh.partfinder.core.domain.repository.AdRepository
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.domain.repository.BusinessNewsRepository

class HomePageViewModelTest {

    @Mock
    private lateinit var router: Router

    @Mock
    private lateinit var businessNewsRepository: BusinessNewsRepository

    @Mock
    private lateinit var adRepository: AdRepository

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var homePageViewModel: HomePageViewModel

    @Before
    fun before() {
        homePageViewModel = HomePageViewModel(
            router, businessNewsRepository, adRepository, authRepository
        )
    }

    @Test
    fun testNewsApiDataFetchSuccess() {

    }

    @Test
    fun testRecommendedAdsDataFetchSuccess() {

    }

    @Test
    fun testNewsApiDataFetchFail() {

    }

    @Test
    fun testRecommendedAdsDataFetchFail() {

    }

}