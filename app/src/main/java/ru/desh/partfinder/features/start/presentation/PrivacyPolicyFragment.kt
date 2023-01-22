package ru.desh.partfinder.features.start.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.desh.partfinder.core.Screens.Auth
import ru.desh.partfinder.core.Screens.PrivacyPolicySource
import ru.desh.partfinder.core.data.properties.PropertiesRepository
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.databinding.FragmentPrivacyPolicyBinding
import javax.inject.Inject

class PrivacyPolicyViewModel @Inject constructor(
    private val propertiesRepository: PropertiesRepository,
    @AppNavigation private val router: Router
) : ViewModel() {

    fun toAuth() = router.navigateTo(Auth())
    fun toPrivacyPolicySource() = router.navigateTo(PrivacyPolicySource())

    suspend fun finishOnboarding() {
        propertiesRepository.setOnboardingFinished()
    }
}

class PrivacyPolicyFragment : Fragment() {
    private lateinit var binding: FragmentPrivacyPolicyBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PrivacyPolicyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[PrivacyPolicyViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            privacyPolicyButtonSource.setOnClickListener {
                viewModel.toPrivacyPolicySource()
            }
            privacyPolicyButtonAccept.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.finishOnboarding()
                }.invokeOnCompletion {
                    viewModel.toAuth()
                }
            }
            privacyPolicyButtonDecline.setOnClickListener {
                requireActivity().finish()
            }
        }
    }
}