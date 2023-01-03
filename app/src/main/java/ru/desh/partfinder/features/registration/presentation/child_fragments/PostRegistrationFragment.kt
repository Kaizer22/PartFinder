package ru.desh.partfinder.features.registration.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import ru.desh.partfinder.core.Screens.HomePage
import ru.desh.partfinder.core.di.AppNavigation
import ru.desh.partfinder.databinding.FragmentPostRegistrationBinding
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import javax.inject.Inject

class PostRegistrationFragment: Fragment() {
    @Inject
    @AppNavigation
    lateinit var router: Router

    @Inject
    @AppNavigation
    lateinit var navigatorHolder: NavigatorHolder

    private lateinit var binding: FragmentPostRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (parentFragment as RegistrationFragment).registrationComponent
            .inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            postRegistrationButtonFinish.setOnClickListener {
                router.navigateTo(HomePage())
            }
        }
    }
}