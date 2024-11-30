package com.example.storie.ui.activity.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.storie.R
import com.example.storie.utils.IdlingResource
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginLogoutUITest {

    private val dummyEmail = "test@example.com"
    private val dummyPassword = "password"
    private val dummyWrongEmail = "wrong@example.com"
    private val dummyWrongPassword = "wrongpassword"

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(IdlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResource.idlingResource)
    }

    @Test
    fun testSuccessfulLogin() {
        onView(withId(R.id.ed_login_email)).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText(dummyPassword), closeSoftKeyboard())
        onView(withId(R.id.btn_register)).perform(click())

        activityRule.scenario.onActivity { activity ->
            onView(withText(R.string.login_succes_dialog))
                .inRoot(withDecorView(not(activity.window.decorView)))
                .check(matches(isDisplayed()))
        }

        testLogout()
    }

    @Test
    fun testFailedLogin() {
        onView(withId(R.id.ed_login_email)).perform(
            typeText(dummyWrongEmail),
            closeSoftKeyboard()
        )
        onView(withId(R.id.ed_login_password)).perform(
            typeText(dummyWrongPassword),
            closeSoftKeyboard()
        )
        onView(withId(R.id.btn_register)).perform(click())

        activityRule.scenario.onActivity { activity ->
            onView(withText(R.string.login_failed_dialog))
                .inRoot(withDecorView(not(activity.window.decorView)))
                .check(matches(isDisplayed()))
        }
    }

    private fun testLogout() {
        onView(withId(R.id.nav_view)).perform(click())

        onView(withId(R.id.button_action_logout)).perform(click())

        onView(withText(R.string.logout_confirmation))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withText(R.string.yes)).perform(click())
    }
}