package ru.desh.partfinder.core

import android.content.Intent
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.desh.partfinder.features.auth.presentation.AuthFragment
import ru.desh.partfinder.features.auth.presentation.CodeEnterFragment
import ru.desh.partfinder.features.auth.presentation.PhoneAuthFragment
import ru.desh.partfinder.features.password_reset.presentation.PasswordResetFragment
import ru.desh.partfinder.features.registration.presentation.NameFormFragment
import ru.desh.partfinder.features.registration.presentation.NewProfileFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
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
    fun PasswordReset() = FragmentScreen {
        PasswordResetFragment()
    }
    fun PhoneAuth() = FragmentScreen{
        PhoneAuthFragment()
    }
    fun CodeEnter() = FragmentScreen {
        CodeEnterFragment()
    }
    fun NameForm() = FragmentScreen {
        NameFormFragment()
    }
    fun Registration() = FragmentScreen {
        RegistrationFragment()
    }
    fun NewProfile() = FragmentScreen {
        NewProfileFragment()
    }
    // endregion

    //endregion

}