package com.example.tactalk

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.tactalk.activity.LoginFragment
import com.example.tactalk.activity.RegisterFragment

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class RegistrationTest {

    @Test
    fun testVisibility_LoginFragment() {
        val activityScenario = ActivityScenario.launch(LoginFragment::class.java)
        onView(ViewMatchers.withId(R.id.edt_email)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.edt_password)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.btn_login)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testVisibility_RegisterFragment() {
        val activityScenario = ActivityScenario.launch(RegisterFragment::class.java)
        onView(ViewMatchers.withId(R.id.edt_name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.edt_email)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.edt_password)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.btn_register)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testLoginTextDisplayed() {
        val activityScenario = ActivityScenario.launch(LoginFragment::class.java)
        onView(ViewMatchers.withId(R.id.edt_email)).check(
            ViewAssertions.matches(
                ViewMatchers.withHint(
                    "Email"
                )
            )
        )
        onView(ViewMatchers.withId(R.id.edt_password)).check(
            ViewAssertions.matches(
                ViewMatchers.withHint(
                    "Password"
                )
            )
        )
        onView(ViewMatchers.withId(R.id.btn_login)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    "Sign in"
                )
            )
        )
    }

    @Test
    fun testRegisterTextDisplayed() {
        val activityScenario = ActivityScenario.launch(RegisterFragment::class.java)
        onView(ViewMatchers.withId(R.id.edt_name)).check(
            ViewAssertions.matches(
                ViewMatchers.withHint(
                    "Name"
                )
            )
        )
        onView(ViewMatchers.withId(R.id.edt_email)).check(
            ViewAssertions.matches(
                ViewMatchers.withHint(
                    "Email"
                )
            )
        )
        onView(ViewMatchers.withId(R.id.edt_password)).check(
            ViewAssertions.matches(
                ViewMatchers.withHint(
                    "Password"
                )
            )
        )
        onView(ViewMatchers.withId(R.id.btn_register)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    "Register"
                )
            )
        )
    }

    @Test
    fun testInvalidLogin(){
        val activityScenario = ActivityScenario.launch(LoginFragment::class.java)
        onView(ViewMatchers.withId(R.id.edt_email)).perform(ViewActions.typeText("Invalid@gmail.com"))
        onView(ViewMatchers.withId(R.id.btn_login)).perform(ViewActions.click())
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    @Test
    fun testInvalidRegister(){
        val activityScenario = ActivityScenario.launch(RegisterFragment::class.java)
        onView(ViewMatchers.withId(R.id.edt_name)).perform(ViewActions.typeText("Invalid"))
        onView(ViewMatchers.withId(R.id.edt_email)).perform(ViewActions.typeText("Invalid@gmail.com"), ViewActions.closeSoftKeyboard())
        onView(ViewMatchers.withId(R.id.btn_register)).perform(ViewActions.click())
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

}