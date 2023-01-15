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
import ru.desh.partfinder.databinding.FragmentSettingsBinding
import ru.desh.partfinder.features.BottomNavigationActivity
import javax.inject.Inject

class AppSettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @AppNavigation private val router: Router
) : ViewModel() {

    fun toMain() = router.navigateTo(Main(true))

    fun signOut() {
        authRepository.signOut()
    }
}

class AppSettingsFragment : Fragment() {

    @Inject
    lateinit var viewModel: AppSettingsViewModel

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
                viewModel.toMain()
                requireActivity().finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as BottomNavigationActivity).showNavigation()
    }
}