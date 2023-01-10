package ru.desh.partfinder.features.ads.presentation

import androidx.fragment.app.Fragment
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.features.BottomNavigationActivity

class AdDetailsFragment(
    private val data: Ad
): Fragment() {
    override fun onResume() {
        super.onResume()
        (requireActivity() as BottomNavigationActivity).hideNavigation()
    }

}