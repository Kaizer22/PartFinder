package ru.desh.partfinder.features.ads.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.launch
import ru.desh.partfinder.R
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.databinding.FragmentCreateAdBinding
import ru.desh.partfinder.features.BottomNavigationActivity
import ru.desh.partfinder.features.ads.di.CreateAdComponent
import javax.inject.Inject


class CreateAdFragment : Fragment() {
    private val navigator: Navigator by lazy {
        AppNavigator(requireActivity(), R.id.create_ad_container, childFragmentManager)
    }

    private lateinit var binding: FragmentCreateAdBinding

    lateinit var createAdComponent: CreateAdComponent
        private set

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CreateAdViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAdComponent = SingleApplicationComponent.getInstance().createAdComponentFactory()
            .create()
        createAdComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[CreateAdViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            createAdButtonBack.setOnClickListener {
                viewModel.back()
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeCreateAdState().collect { createAdState ->
                    when (createAdState) {
                        is CreateAdState.InitState -> {
                            binding.createAdSteppedProgressBar.nextStep()
                            viewModel.toCreateAdTarget()
                        }
                        is CreateAdState.TargetSubmitted -> {
                            binding.createAdSteppedProgressBar.nextStep()
                            viewModel.toCreateAdDescription()
                        }
                        is CreateAdState.DescriptionSubmitted -> {
                            binding.createAdSteppedProgressBar.nextStep()
                            viewModel.toCreateAdFiles()
                        }
                        is CreateAdState.FilesSubmitted -> {
                            binding.createAdSteppedProgressBar.nextStep()
                            viewModel.toCreateAdContacts()
                        }
                        is CreateAdState.AdPublished -> {
                            binding.createAdSteppedProgressBar.nextStep()
                            viewModel.toPostCreateAd()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initInnerNavigation(navigator)
        (activity as BottomNavigationActivity).hideNavigation()
    }

    override fun onPause() {
        viewModel.clearInnerNavigation()
        super.onPause()
    }
}