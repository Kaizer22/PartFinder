package ru.desh.partfinder.features.ads.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import ru.desh.partfinder.R
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.databinding.FragmentHomePageBinding
import ru.desh.partfinder.features.BottomNavigationActivity
import ru.desh.partfinder.features.ads.presentation.adapter.AdsAdapter
import ru.desh.partfinder.features.ads.presentation.adapter.BusinessArticleAdapter
import ru.desh.partfinder.features.ads.presentation.adapter.CategoriesAdapter
import ru.desh.partfinder.features.ads.data.CategoryProvider
import javax.inject.Inject

class HomePageFragment: Fragment() {
    @Inject
    @AppNavigation
    lateinit var router: Router


    @Inject
    lateinit var viewModel: HomePageFragmentViewModel

    private lateinit var infoMessage: SnackbarBuilder
    private lateinit var warningMessage: SnackbarBuilder
    private val onFailureListener = fun(exception: Exception) {
        warningMessage.show()
        Log.d("HOME_PAGE_FRAGMENT", exception.toString())
    }

    private val onRecommendedAdsScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!recyclerView.canScrollVertically(1) && dy > 0)
            {
                infoMessage.show()
                viewModel.requestRecommendedAdsNextPage(viewLifecycleOwner, onFailureListener)
            }
        }
    }

    private lateinit var binding: FragmentHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adsAdapter = AdsAdapter(router)
        val businessArticlesAdapter = BusinessArticleAdapter(router)
        businessArticlesAdapter.submitList(emptyList())
        val categoriesAdapter = CategoriesAdapter(router)
        categoriesAdapter.submitList(CategoryProvider.getCategories(requireContext()))

        infoMessage = SnackbarBuilder(binding.content, layoutInflater, Snackbar.LENGTH_LONG)
            .setType(SnackbarBuilder.Type.PRIMARY)
            .setTitle("Инфо")
            .setText("Загрузка следующей страницы объявлений...")
        val todoMessage = SnackbarBuilder(binding.content, layoutInflater, Snackbar.LENGTH_LONG)
            .setType(SnackbarBuilder.Type.PRIMARY)
            .setTitle(getString(R.string.message_title_todo))
            .setText(getString(R.string.message_text_todo))
        warningMessage = SnackbarBuilder(binding.content, layoutInflater, Snackbar.LENGTH_LONG)
            .setType(SnackbarBuilder.Type.WARNING)
            .setTitle(getString(R.string.message_title_error))
            .setText(getString(R.string.message_text_cannot_load_rec_ads))
        viewModel.state.observe(viewLifecycleOwner) {
            updateUiState(it, adsAdapter, businessArticlesAdapter)
        }
        viewModel.requestBusinessNewsNextPage()
        viewModel.requestRecommendedAdsNextPage(viewLifecycleOwner, onFailureListener)

        binding.apply {
            homePageCategoryList.adapter = categoriesAdapter
            homePageCategoryList.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false)
            homePageNewsList.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, true)
            homePageNewsList.adapter = businessArticlesAdapter
            homePageRecommendationsList.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
            homePageRecommendationsList.adapter = adsAdapter
            homePageRecommendationsList.addOnScrollListener(onRecommendedAdsScrollListener)
            homePageRecommendationsList.updateLayoutParams {
                height = resources.displayMetrics.heightPixels
            }
            homePageUserName.text = viewModel.displayName()

            homePageButtonSearch.setOnClickListener { todoMessage.show() }
            homePageButtonSearchOptions.setOnClickListener { todoMessage.show() }
        }
    }

    private fun updateUiState(state: HomePageState,
    adsAdapter: AdsAdapter, businessArticleAdapter: BusinessArticleAdapter) {
        adsAdapter.submitList(state.recommendedAds)
        businessArticleAdapter.submitList(state.businessArticles)
    }

    override fun onResume() {
        super.onResume()
        (activity as BottomNavigationActivity).showNavigation()
    }
}