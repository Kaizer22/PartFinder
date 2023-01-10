package ru.desh.partfinder.features.ads.presentation.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.databinding.FragmentPostCreateAdBinding
import ru.desh.partfinder.features.ads.presentation.CreateAdFragment
import javax.inject.Inject

class PostCreateAdFragment: Fragment() {
    @Inject
    @AppNavigation
    lateinit var router: Router


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (parentFragment as CreateAdFragment).createAdComponent.inject(this)
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
                router.exit()
            }
        }
    }
}