package ru.desh.partfinder.features

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.desh.partfinder.R
import ru.desh.partfinder.core.Screens.HomePage
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import ru.desh.partfinder.databinding.ActivityBottomNavigationBinding

import javax.inject.Inject

class BottomNavigationActivity: AppCompatActivity() {
    @Inject
    @AppNavigation
    lateinit var router: Router
    @Inject
    @AppNavigation
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator: Navigator = AppNavigator(this, R.id.bottom_navigation_container)

    private lateinit var binding: ActivityBottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        SingleApplicationComponent.getInstance().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.bottomAppBar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_app_bar_menu, menu)
        return true
    }

    fun hideNavigation() {
        binding.bottomNavigationCoordinatorLayout.visibility = View.GONE
    }
    fun showNavigation() {
        binding.bottomNavigationCoordinatorLayout.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
        router.navigateTo(HomePage())
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}