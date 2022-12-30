package ru.desh.partfinder.features.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import ru.desh.partfinder.core.Screens.NameForm
import ru.desh.partfinder.core.Screens.PasswordReset
import ru.desh.partfinder.core.Screens.PhoneAuth
import ru.desh.partfinder.core.Screens.Welcome
import ru.desh.partfinder.core.di.AppNavigation
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.databinding.FragmentAuthBinding
import javax.inject.Inject

class AuthFragment: Fragment() {
    @Inject
    lateinit var viewModel: AuthFragmentViewModel

    @Inject
    @AppNavigation
    lateinit var router: Router
    @Inject
    @AppNavigation
    lateinit var navigatorHolder: NavigatorHolder

    private lateinit var binding: FragmentAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            authButtonSignIn.setOnClickListener {
                val email = authEmailInput.editText?.text.toString()
                val password = authPasswordInput.editText?.text.toString()
                lifecycleScope.launchWhenCreated {
                    viewModel.authWithEmailAndPassword(email, password).collect { result ->
                        if (!result.isException) router.navigateTo(Welcome())
                    }
                }
                //TODO show loading
            }
            textButtonForgotPassword.setOnClickListener {
                router.navigateTo(PasswordReset())
            }
            authButtonPhone.setOnClickListener {
                router.navigateTo(PhoneAuth())
            }
            hintRegisterBlock.authButtonToRegister.setOnClickListener {
                router.navigateTo(NameForm())
            }
        }
    }
}