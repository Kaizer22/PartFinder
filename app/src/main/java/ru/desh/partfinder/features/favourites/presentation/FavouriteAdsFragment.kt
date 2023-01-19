package ru.desh.partfinder.features.favourites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.desh.partfinder.databinding.FragmentFavouriteAdsBinding
import ru.desh.partfinder.features.BottomNavigationActivity

class FavouriteAdsFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteAdsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteAdsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as BottomNavigationActivity).showNavigation()
    }
}