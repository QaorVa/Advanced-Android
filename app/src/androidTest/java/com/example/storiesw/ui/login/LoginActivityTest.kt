package com.example.storiesw.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.storiesw.R
import com.example.storiesw.ui.home.HomeActivity
import com.example.storiesw.ui.settings.SettingsActivity
import com.example.storiesw.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        Intents.init()
    }
    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        Intents.release()
    }

    @Test
    fun loadLoginScreen() {
        onView(withId(R.id.ed_login_email)).perform(typeText("klanton@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText("antonanton"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        Intents.intended(hasComponent(HomeActivity::class.java.name))
    }

    @Test
    fun loadHomeScreen() {
        loadLoginScreen()

        onView(withId(R.id.settings_menu)).perform(click())

        Intents.intended(hasComponent(SettingsActivity::class.java.name))

    }

    @Test
    fun loadSettingsScreen() {
        loadHomeScreen()

        onView(withId(R.id.logout_button)).perform(click())

        Intents.intended(hasComponent(LoginActivity::class.java.name))
    }

}