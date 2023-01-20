package ru.desh.partfinder.features.registration.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.launch
import ru.desh.partfinder.R
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.databinding.FragmentRegistrationBinding
import ru.desh.partfinder.features.registration.di.RegistrationComponent
import ru.desh.partfinder.features.registration.presentation.child_fragments.RegistrationConfirmationFragment
import javax.inject.Inject

class RegistrationFragment : Fragment() {
    @Inject
    lateinit var viewModel: RegistrationViewModel

    lateinit var registrationComponent: RegistrationComponent
        private set

    enum class RegistrationType {
        PHONE, EMAIL
    }

    private lateinit var binding: FragmentRegistrationBinding
    private val navigator: Navigator by lazy {
        AppNavigator(
            requireActivity(), R.id.registration_container,
            childFragmentManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationComponent = SingleApplicationComponent.getInstance()
            .registrationComponentFactory()
            .create()
        registrationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            registrationButtonBack.setOnClickListener {
                viewModel.back()
            }
        }
        viewModel.toRegistrationMethod()
        lifecycleScope.launch {
            viewModel.observeRegistrationState().collect { regState ->
                when (regState) {
                    is RegistrationState.RegistrationMethodSelected -> {
                        binding.registrationSteppedProgressBar.nextStep()
                        viewModel.toRegistrationData(regState.registrationType)
                    }
                    is RegistrationState.PhoneInputFinished -> {
                        val args = Bundle()
                        args.putString(
                            RegistrationConfirmationFragment.PHONE_NUMBER_ARGUMENT,
                            regState.phoneNumber
                        )
                        viewModel.toPhoneConfirmation(args)
                        binding.registrationSteppedProgressBar.nextStep()
                    }
                    is RegistrationState.EmailInputFinished -> {
                        viewModel.toEmailConfirmation()
                        binding.registrationSteppedProgressBar.nextStep()
                    }
                    is RegistrationState.DataConfirmed -> {
                        binding.registrationSteppedProgressBar.nextStep()
                        viewModel.toRegistrationNameForm()
                    }
                    is RegistrationState.RegistrationFinished -> {
                        binding.registrationSteppedProgressBar.nextStep()
                        viewModel.toPostRegistration()
                    }
                    is RegistrationState.InitState -> {}
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initInnerNavigation(navigator)

    }

    override fun onPause() {
        viewModel.clearInnerNavigation()
        super.onPause()
    }
}
