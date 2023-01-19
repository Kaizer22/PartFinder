package ru.desh.partfinder.features.ads.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.databinding.FragmentAdDetailsBinding
import ru.desh.partfinder.features.BottomNavigationActivity

class AdDetailsFragment(
    private val data: Ad
) : Fragment() {

    private lateinit var binding: FragmentAdDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as BottomNavigationActivity).hideNavigation()
    }

}