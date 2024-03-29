package ru.desh.partfinder.features.start.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import ru.desh.partfinder.core.Screens.Main
import ru.desh.partfinder.core.data.properties.PropertiesRepository
import ru.desh.partfinder.core.di.SingleApplicationComponent
import ru.desh.partfinder.core.di.module.AppNavigation
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    @Inject
    @AppNavigation
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    @AppNavigation
    lateinit var router: Router

    @Inject
    lateinit var propertiesRepository: PropertiesRepository

    private val navigator: Navigator = AppNavigator(this, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)

        lifecycleScope.launch {
            // TODO do preparation work:
            // - Try to auth from with data from cache
            propertiesRepository.getOnBoardingFlag().collectLatest { onBoardingFlag ->
                router.navigateTo(Main(onBoardingFlag))
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}