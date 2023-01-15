package ru.desh.partfinder.features.ads.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.desh.partfinder.core.domain.model.AdCategory
import ru.desh.partfinder.databinding.FragmentCategorySearchBinding
import ru.desh.partfinder.features.BottomNavigationActivity

class CategorySearchFragment(
    private val adCategory: AdCategory
) : Fragment() {
    private lateinit var binding: FragmentCategorySearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategorySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as BottomNavigationActivity).hideNavigation()
    }
}