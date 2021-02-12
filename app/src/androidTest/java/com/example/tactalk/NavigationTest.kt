package com.example.tactalk

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class NavigationTest {

    @Test
    fun testFragmentDisplayed() {
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<MainMenuFragment>(fragmentArgs = bundle)

        onView(withId(R.id.ManageTeam_button)).check(matches(isDisplayed()))
        onView(withId(R.id.SetUpMatchButton)).check(matches(isDisplayed()))
        onView(withId(R.id.Record)).check(matches(isDisplayed()))
    }

    @Test
    fun testFragmentNavigation(){
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<MainMenuFragment>(fragmentArgs = bundle)

        onView(withId(R.id.ManageTeam_button)).perform(click())
        //wait
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        onView(withId(R.id.textView4)).check(matches(isDisplayed()))
        onView(withId(R.id.textView5)).check(matches(isDisplayed()))
        onView(withId(R.id.scrollView3)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        pressBack()

    }
}