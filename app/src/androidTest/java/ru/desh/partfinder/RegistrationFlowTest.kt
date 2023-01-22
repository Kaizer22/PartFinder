package ru.desh.partfinder

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import ru.desh.partfinder.core.MainActivity
import ru.desh.partfinder.utils.waitForView
import ru.desh.partfinder.utils.waitForViewToDisappear
import kotlin.random.Random

class RegistrationFlowTest {
    @get:Rule
    val mainActivityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun registerNewUserViaEmail() {
        val rnd = Random(System.currentTimeMillis())
        val testEmail = "example${rnd.nextLong()}@gmail.com"
        val testPassword = "Qwerty123$"

        onView(withId(R.id.welcome_button_ok))
            .perform(click())
        onView(withId(R.id.privacy_policy_button_accept))
            .perform(click())
        onView(withId(R.id.auth_button_to_register))
            .perform(click())
        onView(withId(R.id.registration_method_button_email))
            .perform(click())

        // Turn off animations
        // https://developer.android.com/training/testing/espresso/setup#set-up-environment
        onView(withId(R.id.registration_data_email_input))
            .perform(typeText(testEmail))
        onView(withId(R.id.registration_data_password_input))
            .perform(typeText(testPassword))
        onView(withId(R.id.registration_data_password_repeat_input))
            .perform(
                typeText(testPassword),
                closeSoftKeyboard()
            )
        onView(withId(R.id.registration_data_button_send))
            .perform(click())

        onView(isRoot()).perform(
            waitForView(
                R.id.registration_confirmation_email_animation,
                3000
            )
        )
        onView(withId(R.id.registration_confirmation_button_email_next))
            .perform(click())

        onView(withId(R.id.name_form_name_input))
            .perform(typeText("Name"))
        onView(withId(R.id.name_form_surname_input))
            .perform(typeText("Surname"))
        onView(withId(R.id.name_form_third_name_input))
            .perform(typeText("LastName"), closeSoftKeyboard())
        onView(withId(R.id.name_form_button_send))
            .perform(click())

        onView(isRoot()).perform(
            waitForView(
                R.id.post_registration_button_finish,
                3000
            )
        )
        onView(withId(R.id.post_registration_button_finish)).perform(click())

        onView(withId(R.id.auth_email_input)).perform(typeText(testEmail))
        onView(withId(R.id.auth_password_input)).perform(
            typeText(testPassword),
            closeSoftKeyboard()
        )
        onView(withId(R.id.auth_button_sign_in)).perform(click())

        waitForViewToDisappear(R.id.auth_button_sign_in, 2000)
    }
}