package com.example.storie.ui.main.add_story

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.example.storie.MainActivity
import com.example.storie.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddStoryFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testAddStory() {
        onView(withId(R.id.navigation_add)).perform(click())

        onView(withId(R.id.navigation_add)).check(matches(isDisplayed()))

        Intents.init()
        onView(withId(R.id.btn_gallery)).perform(click())
        Intents.intended(hasAction(Intent.ACTION_GET_CONTENT))
        Intents.release()

        Intents.init()
        onView(withId(R.id.btn_camera_x)).perform(click())
        Intents.intended(hasAction("android.media.action.IMAGE_CAPTURE"))
        handlePermissionDialog()

        onView(withId(R.id.ed_add_description)).perform(
            typeText("Add story test!"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.switch_location)).perform(click())

        onView(withId(R.id.button_add)).perform(click())

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))

    }

    private fun handlePermissionDialog() {
        val device = UiDevice.getInstance(getInstrumentation())
        val allowButton = device.findObject(UiSelector().text("While using the app"))
        if (allowButton.exists()) {
            allowButton.click()
        }
    }
}
