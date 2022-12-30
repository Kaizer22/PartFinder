package ru.desh.partfinder.core

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.desh.partfinder.R
import ru.desh.partfinder.core.Screens.Auth
import ru.desh.partfinder.core.Screens.Welcome
import ru.desh.partfinder.core.di.AppNavigation
import ru.desh.partfinder.core.di.SingleApplicationComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    @AppNavigation
    lateinit var router: Router
    @Inject
    @AppNavigation
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator: Navigator = AppNavigator(this, R.id.app_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SingleApplicationComponent.getInstance().inject(this)
        val startFr = intent.getStringExtra(Screens.START_FRAGMENT)
        when (startFr) {
            //TODO
            "onboarding" -> router.navigateTo(Welcome())
            "auth" -> router.navigateTo(Auth())
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