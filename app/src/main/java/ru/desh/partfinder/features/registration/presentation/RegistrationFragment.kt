package ru.desh.partfinder.features.registration.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.*
import ru.desh.partfinder.R
import ru.desh.partfinder.core.Screens.Post_Registration
import ru.desh.partfinder.core.Screens.Registration_Confirmation
import ru.desh.partfinder.core.Screens.Registration_Data
import ru.desh.partfinder.core.Screens.Registration_Method
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.databinding.FragmentRegistrationBinding
import ru.desh.partfinder.features.registration.presentation.child_fragments.PostRegistrationFragment
import ru.desh.partfinder.features.registration.presentation.child_fragments.RegistrationConfirmationFragment
import ru.desh.partfinder.features.registration.presentation.child_fragments.RegistrationDataFragment
import ru.desh.partfinder.features.registration.presentation.child_fragments.RegistrationMethodFragment
import javax.inject.Inject

class RegistrationFragment: Fragment() {

    @Inject
    lateinit var router: Router
    @Inject
    lateinit var navigatorHolder: NavigatorHolder

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
                            PostRegistrationFragment()
                        )
                        Registration_Method().screenKey -> changeStage(
                            RegistrationMethodFragment()
                        )
                        Registration_Data().screenKey -> changeStage(
                            RegistrationDataFragment()
                        )
                        Registration_Confirmation().screenKey -> changeStage(
                            RegistrationConfirmationFragment()
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
        SingleApplicationComponent.getInstance().inject(this)
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
            registrationButtonNextStep.setOnClickListener {
                registrationSteppedProgressBar.nextStep()
            }
            registrationButtonBack.setOnClickListener {
                registrationSteppedProgressBar.previousStep()
            }
        }
        router.navigateTo(Registration_Method())
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}