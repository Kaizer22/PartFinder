package ru.desh.partfinder.core

import android.content.Intent
import android.net.Uri
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.desh.partfinder.core.domain.model.Ad
import ru.desh.partfinder.features.BottomNavigationActivity
import ru.desh.partfinder.features.ads.presentation.AdDetailsFragment
import ru.desh.partfinder.features.ads.presentation.CreateAdFragment
import ru.desh.partfinder.features.ads.presentation.HomePageFragment
import ru.desh.partfinder.features.auth.presentation.AuthFragment
import ru.desh.partfinder.features.auth.presentation.CodeEnterFragment
import ru.desh.partfinder.features.auth.presentation.PhoneAuthFragment
import ru.desh.partfinder.features.auth.presentation.PasswordResetFragment
import ru.desh.partfinder.features.registration.presentation.child_fragments.RegistrationNameFragment
import ru.desh.partfinder.features.registration.presentation.UserFormFragment
import ru.desh.partfinder.features.registration.presentation.RegistrationFragment
import ru.desh.partfinder.features.registration.presentation.child_fragments.*
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
    fun BottomNavigation() = ActivityScreen {
        Intent(it, BottomNavigationActivity::class.java)
    }
    //endregion

    // region External

    fun NewsArticleSource(url: String) = ActivityScreen {
        Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }

    // endregion

    // region Fragment

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
    // endregion

    // region Registration
    fun Registration_Name() = FragmentScreen {
        RegistrationNameFragment()
    }
    fun Registration() = FragmentScreen {
        RegistrationFragment()
    }
    fun NewProfile() = FragmentScreen {
        UserFormFragment()
    }
    fun Registration_Method() = FragmentScreen {
        RegistrationMethodFragment()
    }
    fun Registration_Data(registrationType: RegistrationFragment.RegistrationType) =
        FragmentScreen {
            RegistrationDataFragment(registrationType)
    }
    fun Registration_Confirmation(registrationState: RegistrationFragment.RegistrationType,
        phoneNumber: String) = FragmentScreen {
        RegistrationConfirmationFragment(registrationState, phoneNumber)
    }
    fun Post_Registration() = FragmentScreen {
        PostRegistrationFragment()
    }
    fun UserForm() = FragmentScreen {
        UserFormFragment()
    }
    fun Pre_UserForm() = FragmentScreen {
        PreUserFormFragment()
    }
    fun UserForm_Orgainisation() = FragmentScreen {
        UserFormOrganisationFragment()
    }
    fun UserForm_Activity() = FragmentScreen {
        UserFormActivityFragment()
    }
    fun UserForm_About() = FragmentScreen {
        UserFormAboutFragment()
    }
    fun UserForm_Survey() = FragmentScreen {
        UserFormSurveyFragment()
    }
    // endregion

    // region Auth
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
    // endregion

    // region Ads

    fun HomePage() = FragmentScreen {
        HomePageFragment()
    }

    fun AdDetails(ad: Ad) = FragmentScreen {
        AdDetailsFragment(ad)
    }

    fun CreateAd() = FragmentScreen {
        CreateAdFragment()
    }

    fun CreateAd_Target() = FragmentScreen {
        CreateAdFragment()
    }

    fun CreateAd_Description() = FragmentScreen {
        CreateAdFragment()
    }

    fun CreateAd_Files() = FragmentScreen {
        CreateAdFragment()
    }

    fun CreateAd_Contacts() = FragmentScreen {
        CreateAdFragment()
    }

    fun Post_CreateAd() = FragmentScreen {
        CreateAdFragment()
    }
    // endregion

    // endregion

}