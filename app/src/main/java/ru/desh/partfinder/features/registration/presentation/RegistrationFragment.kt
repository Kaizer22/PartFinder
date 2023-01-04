package ru.desh.partfinder.features.registration.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.terrakok.cicerone.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.desh.partfinder.R
import ru.desh.partfinder.core.Screens.Post_Registration
import ru.desh.partfinder.core.Screens.Registration_Confirmation
import ru.desh.partfinder.core.Screens.Registration_Data
import ru.desh.partfinder.core.Screens.Registration_Method
import ru.desh.partfinder.core.Screens.Registration_Name
import ru.desh.partfinder.core.di.AppNavigation
import ru.desh.partfinder.core.di.RegistrationNavigation
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.databinding.FragmentRegistrationBinding
import ru.desh.partfinder.features.registration.di.RegistrationComponent
import ru.desh.partfinder.features.registration.presentation.child_fragments.*
import javax.inject.Inject

class RegistrationFragment: Fragment() {
    @Inject
    @AppNavigation
    lateinit var router: Router
    @Inject
    @AppNavigation
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    @RegistrationNavigation
    lateinit var innerRouter: Router
    @Inject
    @RegistrationNavigation
    lateinit var innerNavigatorHolder: NavigatorHolder


    lateinit var registrationComponent: RegistrationComponent
        private set
    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.InitState)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    private val registrationMethodFragment = RegistrationMethodFragment()
    private lateinit var registrationDataFragment: RegistrationDataFragment
    private lateinit var registrationConfirmationFragment: RegistrationConfirmationFragment
    private val registrationNameFragment = RegistrationNameFragment()
    private val postRegistrationFragment = PostRegistrationFragment()

    enum class RegistrationType{
        PHONE, EMAIL
    }

    private lateinit var binding: FragmentRegistrationBinding
    private val navigator = object : Navigator {
        override fun applyCommands(commands: Array<out Command>) {
            for (command in commands) applyCommand(command)
        }

        private fun applyCommand(command: Command) {
            when(command){
                is Back -> {}
                is Forward -> {
                    when(command.screen.screenKey) {
                        Post_Registration().screenKey -> changeStage(
                            postRegistrationFragment
                        )
                        Registration_Method().screenKey -> changeStage(
                            registrationMethodFragment
                        )
                        Registration_Data(RegistrationType.EMAIL).screenKey -> changeStage(
                            registrationDataFragment
                        )
                        Registration_Confirmation(RegistrationType.EMAIL, "").screenKey -> changeStage(
                            registrationConfirmationFragment
                        )
                        Registration_Name().screenKey -> changeStage(
                            registrationNameFragment
                        )
                    }
                }
            }
        }
        private fun changeStage(fragment: Fragment) {
            childFragmentManager.beginTransaction()
                .replace(R.id.registration_container, fragment).commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationComponent = SingleApplicationComponent.getInstance()
            .registrationComponentFactory()
            .create(_registrationState)
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
                registrationSteppedProgressBar.previousStep()
            }
            registrationButtonBack.setOnClickListener {
                router.exit()
            }
        }
        innerRouter.navigateTo(Registration_Method())
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registrationState.collect{ regState ->
                    when (regState){
                        is RegistrationState.RegistrationMethodSelected -> {
                            when(regState.registrationType) {
                                RegistrationType.PHONE -> Log.d("TEST", "PHONE")
                                RegistrationType.EMAIL -> Log.d("TEST", "EMAIL")
                            }
                            //TODO refactor
                            binding.registrationSteppedProgressBar.nextStep()
                            registrationDataFragment = RegistrationDataFragment(regState.registrationType)
                            innerRouter.navigateTo(Registration_Data(regState.registrationType))
                        }
                        is RegistrationState.PhoneInputFinished -> {
                            registrationConfirmationFragment = RegistrationConfirmationFragment(
                                RegistrationType.PHONE, regState.phoneNumber)
                            innerRouter.navigateTo(Registration_Confirmation(RegistrationType.PHONE, regState.phoneNumber))
                            binding.registrationSteppedProgressBar.nextStep()
                        }
                        is RegistrationState.EmailInputFinished -> {
                            registrationConfirmationFragment = RegistrationConfirmationFragment(
                                RegistrationType.EMAIL, ""
                            )
                            innerRouter.navigateTo(Registration_Confirmation(RegistrationType.EMAIL, ""))
                            binding.registrationSteppedProgressBar.nextStep()
                        }
                        is RegistrationState.DataConfirmed -> {
                            binding.registrationSteppedProgressBar.nextStep()
                            innerRouter.navigateTo(Registration_Name())
                        }
                        is RegistrationState.RegistrationFinished -> {
                            binding.registrationSteppedProgressBar.nextStep()
                            innerRouter.navigateTo(Post_Registration())
                        }
                        is RegistrationState.InitState -> {}
                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        innerNavigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        innerNavigatorHolder.removeNavigator()
        super.onPause()
    }
}