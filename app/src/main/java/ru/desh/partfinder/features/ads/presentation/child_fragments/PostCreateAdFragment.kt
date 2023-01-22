package ru.desh.partfinder.features.ads.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Router
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.databinding.FragmentPostCreateAdBinding
import ru.desh.partfinder.features.ads.presentation.CreateAdFragment
import javax.inject.Inject

class PostCreateAdViewModel @Inject constructor(
    @AppNavigation private val router: Router
) : ViewModel() {
    fun back() = router.exit()
}

class PostCreateAdFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PostCreateAdViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (parentFragment as CreateAdFragment).createAdComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[PostCreateAdViewModel::class.java]
    }

    private lateinit var binding: FragmentPostCreateAdBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostCreateAdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            postCreateAdButtonNext.setOnClickListener {
                viewModel.back()
            }
        }
    }
}