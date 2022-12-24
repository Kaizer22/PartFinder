package ru.desh.partfinder.core

import android.content.Intent
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.desh.partfinder.features.auth.presentation.AuthFragment
import ru.desh.partfinder.features.start.presentation.OnBoardingFragment
import ru.desh.partfinder.features.start.presentation.PrivacyPolicyFragment
import ru.desh.partfinder.features.start.presentation.WelcomeFragment

object Screens {

    //region Activity
    const val START_FRAGMENT = "start_fragment"
    fun Main(startFragment: String) = ActivityScreen {
        val i = Intent(it, MainActivity::class.java)
        i.putExtra(START_FRAGMENT, startFragment)
    }
    //endregion

    //region Fragment

    // region Start
    fun Welcome() = FragmentScreen {
        WelcomeFragment()
    }
    fun OnBoarding() = FragmentScreen {
        OnBoardingFragment()
    }
    fun PrivacyPolicy() = FragmentScreen {
        PrivacyPolicyFragment()
    }

    fun Auth() = FragmentScreen {
        AuthFragment()
    }
    // endregion

    //endregion

}