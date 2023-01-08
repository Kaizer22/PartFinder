package ru.desh.partfinder.features.ads.presentation

import androidx.fragment.app.Fragment
import ru.desh.partfinder.features.BottomNavigationActivity

class CategorySearchFragment(
    val category: String
): Fragment() {
    override fun onResume() {
        super.onResume()
        (activity as BottomNavigationActivity).hideNavigation()
    }
}