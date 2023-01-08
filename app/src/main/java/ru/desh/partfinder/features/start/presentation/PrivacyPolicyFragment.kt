package ru.desh.partfinder.features.start.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.desh.partfinder.core.Screens.Auth
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.utils.exception.properties.PropertiesRepository
import ru.desh.partfinder.databinding.FragmentPrivacyPolicyBinding
import javax.inject.Inject

class PrivacyPolicyFragmentViewModel @Inject constructor(
    private val propertiesRepository: PropertiesRepository
): ViewModel() {
    suspend fun finishOnboarding(){
        propertiesRepository.setOnboardingFinished()
    }
}
class PrivacyPolicyFragment: Fragment() {
    @Inject
    lateinit var viewModel: PrivacyPolicyFragmentViewModel

    @Inject
    @AppNavigation
    lateinit var router: Router

    private lateinit var binding: FragmentPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
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
        binding.apply{
            privacyPolicyButtonAccept.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.finishOnboarding()
                }.invokeOnCompletion {
                    router.navigateTo(Auth())
                }
            }
            privacyPolicyButtonDecline.setOnClickListener {
                requireActivity().finish()
            }
        }
    }
}