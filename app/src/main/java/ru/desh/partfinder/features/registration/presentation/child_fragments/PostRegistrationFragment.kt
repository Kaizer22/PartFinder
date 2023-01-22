package ru.desh.partfinder.features.registration.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Router
import ru.desh.partfinder.core.Screens.Auth
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.databinding.FragmentPostRegistrationBinding
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import javax.inject.Inject

class PostRegistrationViewModel @Inject constructor(
    @AppNavigation private val router: Router
) : ViewModel() {
    fun toAuth() = router.navigateTo(Auth())
}

class PostRegistrationFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PostRegistrationViewModel

    private lateinit var binding: FragmentPostRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (parentFragment as RegistrationFragment).registrationComponent
            .inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[PostRegistrationViewModel::class.java]
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
        binding.apply {
            postRegistrationButtonFinish.setOnClickListener {
                viewModel.toAuth()
            }
        }
    }
}