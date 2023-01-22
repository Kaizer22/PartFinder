package ru.desh.partfinder.features.start.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Router
import ru.desh.partfinder.core.Screens.PrivacyPolicy
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.databinding.FragmentWelcomeBinding
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    @AppNavigation private val router: Router
) : ViewModel() {
    fun toPrivacyPolicy() = router.navigateTo(PrivacyPolicy())
}

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: WelcomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[WelcomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.welcomeButtonOk.setOnClickListener {
            viewModel.toPrivacyPolicy()
        }
    }
}