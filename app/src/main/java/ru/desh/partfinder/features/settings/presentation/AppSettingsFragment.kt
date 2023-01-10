package ru.desh.partfinder.features.settings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import ru.desh.partfinder.core.Screens.Main
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.core.domain.repository.AuthRepository
import ru.desh.partfinder.databinding.FragmentPostCreateAdBinding
import ru.desh.partfinder.databinding.FragmentSettingsBinding
import ru.desh.partfinder.features.BottomNavigationActivity
import ru.desh.partfinder.features.ads.presentation.CreateAdFragment
import javax.inject.Inject

class AppSettingsFragmentViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    fun signOut() {
        authRepository.signOut()
    }
}

class AppSettingsFragment: Fragment() {

    @Inject
    lateinit var viewModel: AppSettingsFragmentViewModel

    @Inject
    @AppNavigation
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
    }

    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            settingsCardSignOut.setOnClickListener {
                viewModel.signOut()
                router.navigateTo(Main(true))
                requireActivity().finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as BottomNavigationActivity).showNavigation()
    }
}