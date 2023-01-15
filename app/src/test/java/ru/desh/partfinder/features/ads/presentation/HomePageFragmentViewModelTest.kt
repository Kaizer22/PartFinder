package ru.desh.partfinder.features.ads.presentation

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import ru.desh.partfinder.core.domain.repository.AdRepository
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.core.domain.repository.BusinessNewsRepository

class HomePageFragmentViewModelTest {

    @Mock
    private lateinit var businessNewsRepository: BusinessNewsRepository

    @Mock
    private lateinit var adRepository: AdRepository

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var homePageFragmentViewModel: HomePageFragmentViewModel

    @Before
    fun before() {
        homePageFragmentViewModel = HomePageFragmentViewModel(
            businessNewsRepository, adRepository, authRepository
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