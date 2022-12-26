package ru.desh.partfinder.features.start.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import ru.desh.partfinder.core.Screens.Main
import ru.desh.partfinder.core.data.properties.PropertiesRepository
import ru.desh.partfinder.core.di.SingleApplicationComponent
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity: AppCompatActivity() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    @Inject
    lateinit var router: Router
    @Inject
    lateinit var propertiesRepository: PropertiesRepository

    private val navigator: Navigator = AppNavigator(this, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SingleApplicationComponent.getInstance().inject(this)

        GlobalScope.launch {
            // TODO fix scope and do some preparation work
            // - Get on boarding flag
            // - Try to auth from cache, refresh token
            // - Get data for main page
            propertiesRepository.getOnBoardingFlag().collectLatest { onBoardingFlag ->
                val startFragment = if (onBoardingFlag) "auth" else "onboarding"
                router.navigateTo(Main(startFragment))
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