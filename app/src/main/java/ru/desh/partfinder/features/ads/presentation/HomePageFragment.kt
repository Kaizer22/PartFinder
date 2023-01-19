package ru.desh.partfinder.features.ads.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.desh.partfinder.R
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.core.domain.model.AdCategory
import ru.desh.partfinder.core.ui.SnackbarBuilder
import ru.desh.partfinder.databinding.FragmentHomePageBinding
import ru.desh.partfinder.databinding.ItemAdCardBinding
import ru.desh.partfinder.features.BottomNavigationActivity
import ru.desh.partfinder.features.ads.data.CategoryProvider
import ru.desh.partfinder.features.ads.presentation.adapter.*
import javax.inject.Inject

class HomePageFragment : Fragment() {
    @Inject
    lateinit var viewModel: HomePageViewModel

    private lateinit var infoMessage: SnackbarBuilder
    private lateinit var warningMessage: SnackbarBuilder

    private val onRecommendedAdsScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!recyclerView.canScrollVertically(1) && dy > 0) {
                infoMessage.show()
                lifecycleScope.launch {
                    viewModel.requestRecommendedAdsNextPage()
                }
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
        initRecyclerViews()
        val parentActivityContent = requireActivity().findViewById<ViewGroup>(R.id.content)
        infoMessage = SnackbarBuilder(parentActivityContent, layoutInflater, Snackbar.LENGTH_LONG)
            .setType(SnackbarBuilder.Type.PRIMARY)
            .setTitle("Инфо")
            .setText("Загрузка следующей страницы объявлений...")
        val todoMessage =
            SnackbarBuilder(parentActivityContent, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.SECONDARY)
                .setTitle(getString(R.string.message_title_todo))
                .setText(getString(R.string.message_text_todo))
        warningMessage =
            SnackbarBuilder(parentActivityContent, layoutInflater, Snackbar.LENGTH_LONG)
                .setType(SnackbarBuilder.Type.WARNING)
                .setTitle(getString(R.string.message_title_error))
                .setText(getString(R.string.message_text_cannot_load_rec_ads))
        viewModel.homePageState.observe(viewLifecycleOwner) {
            updateUiState(it, adsAdapter, businessArticlesAdapter)
        }
        lifecycleScope.launchWhenCreated {
            viewModel.requestBusinessNewsNextPage()
            try {
                viewModel.requestRecommendedAdsNextPage()
            } catch (e: Exception) {
                warningMessage.show()
                Log.d("HOME_PAGE_FRAGMENT", e.toString())
            }
        }

        binding.apply {
            homePageUserName.text = viewModel.displayName()
            homePageButtonSearch.setOnClickListener { todoMessage.show() }
            homePageButtonSearchOptions.setOnClickListener { todoMessage.show() }
        }
    }

    private lateinit var adsAdapter: AdsAdapter
    private lateinit var businessArticlesAdapter: BusinessArticleAdapter

    private fun initRecyclerViews() {
        binding.apply {
            val todoMessage: SnackbarBuilder = SnackbarBuilder(
                requireActivity().findViewById(R.id.content) as ViewGroup,
                layoutInflater,
                Snackbar.LENGTH_LONG
            ).setType(SnackbarBuilder.Type.SECONDARY)
                .setTitle(getString(R.string.message_title_todo))
                .setText(getString(R.string.message_text_todo))

            val adsActionListener = object : AdsActionListener {
                override fun onAddFavourite(ad: Ad) {
                    todoMessage.show()
                }

                override fun onPressLike(
                    ad: Ad,
                    binding: ItemAdCardBinding,
                    isLiked: Boolean,
                    isDisliked: Boolean
                ) {
                    todoMessage.show()
                }

                override fun onPressDislike(
                    ad: Ad,
                    binding: ItemAdCardBinding,
                    isLiked: Boolean,
                    isDisliked: Boolean
                ) {
                    todoMessage.show()
                }

                override fun onAdsDetails(ad: Ad) {
                    viewModel.toAdDetails(ad)
                }
            }

            val businessArticlesActionListener = object : BusinessArticlesActionListener {
                override fun onBusinessArticleSource(url: String) {
                    viewModel.toBusinessArticleSource(url)
                }
            }

            val adCategoriesActionListener = object : AdCategoriesActionListener {
                override fun onCategory(adCategory: AdCategory) {
                    viewModel.toAdCategorySearch(adCategory)
                }
            }

            adsAdapter = AdsAdapter(adsActionListener)
            businessArticlesAdapter = BusinessArticleAdapter(businessArticlesActionListener)
            businessArticlesAdapter.submitList(emptyList())
            val adCategoriesAdapter = AdCategoriesAdapter(adCategoriesActionListener)
            adCategoriesAdapter.submitList(CategoryProvider.getCategories(requireContext()))

            homePageCategoryList.adapter = adCategoriesAdapter
            homePageCategoryList.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
            homePageNewsList.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, true
            )
            homePageNewsList.adapter = businessArticlesAdapter
            homePageRecommendationsList.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )
            homePageRecommendationsList.adapter = adsAdapter
            homePageRecommendationsList.addOnScrollListener(onRecommendedAdsScrollListener)
            homePageRecommendationsList.updateLayoutParams {
                height = resources.displayMetrics.heightPixels
            }
        }
    }

    private fun updateUiState(
        state: HomePageViewModel.HomePageState,
        adsAdapter: AdsAdapter, businessArticleAdapter: BusinessArticleAdapter
    ) {
        adsAdapter.submitList(state.recommendedAds)
        businessArticleAdapter.submitList(state.businessArticles)
    }

    override fun onResume() {
        super.onResume()
        (activity as BottomNavigationActivity).showNavigation()
    }
}