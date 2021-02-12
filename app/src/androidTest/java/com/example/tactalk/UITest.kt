package com.example.tactalk

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class UITest {

    @Test
    fun testActivity_inView1() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_login_page)).check(matches(isDisplayed()))
    }

    @Test
    fun testActivity_inView2() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_register_page)).check(matches(isDisplayed()))
    }

    @Test
    fun testActivity_inView3() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.GoMainMenu)).check(matches(isDisplayed()))
    }

    @Test
    fun testVisibility_mainActivity() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.Logo)).check(matches(isDisplayed())) // method 1
        onView(withId(R.id.Appname)).check(matches(withEffectiveVisibility(Visibility.VISIBLE))) // method 2
    }

}