package com.coral.activities

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.coral.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Matchers.not

@RunWith(AndroidJUnit4::class)
class SignInActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(SignInActivity::class.java)

    @Before
    fun setup() {
        // Clear any existing data or preferences if needed
    }

    // Basic UI Element Tests
    @Test
    fun testAllUIElementsAreDisplayed() {
        onView(withId(R.id.emailInputLayout))
            .check(matches(isDisplayed()))
        onView(withId(R.id.passwordInputLayout))
            .check(matches(isDisplayed()))
        onView(withId(R.id.signInButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.signUpButton))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testInitialUIState() {
        onView(withId(R.id.emailInput))
            .check(matches(withText("")))
        onView(withId(R.id.passwordInput))
            .check(matches(withText("")))
        onView(withId(R.id.signInButton))
            .check(matches(isEnabled()))
        onView(withId(R.id.progressBar))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    // Email Validation Tests
    @Test
    fun testEmptyEmailValidation() {
        onView(withId(R.id.signInButton)).perform(click())
        onView(withId(R.id.emailInputLayout))
            .check(matches(hasDescendant(withText(R.string.error_required_field))))
    }

    @Test
    fun testInvalidEmailFormat() {
        onView(withId(R.id.emailInput))
            .perform(typeText("invalid-email"), closeSoftKeyboard())
        onView(withId(R.id.signInButton)).perform(click())
        onView(withId(R.id.emailInputLayout))
            .check(matches(hasDescendant(withText(R.string.error_invalid_email))))
    }

    // Password Validation Tests
    @Test
    fun testEmptyPasswordValidation() {
        onView(withId(R.id.emailInput))
            .perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.signInButton)).perform(click())
        onView(withId(R.id.passwordInputLayout))
            .check(matches(hasDescendant(withText(R.string.error_required_field))))
    }

    @Test
    fun testShortPasswordValidation() {
        onView(withId(R.id.emailInput))
            .perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordInput))
            .perform(typeText("12345"), closeSoftKeyboard())
        onView(withId(R.id.signInButton)).perform(click())
        onView(withId(R.id.passwordInputLayout))
            .check(matches(hasDescendant(withText(R.string.error_invalid_password))))
    }

    // Navigation Tests
    @Test
    fun testSignUpButtonNavigation() {
        onView(withId(R.id.signUpButton))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.sign_up)))
            .perform(click())
    }

    // Loading State Tests
    @Test
    fun testLoadingStateUIUpdates() {
        // Enter valid credentials
        onView(withId(R.id.emailInput))
            .perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordInput))
            .perform(typeText("password123"), closeSoftKeyboard())

        // Click sign in
        onView(withId(R.id.signInButton)).perform(click())

        // Verify loading state
        onView(withId(R.id.progressBar))
            .check(matches(isDisplayed()))
        onView(withId(R.id.signInButton))
            .check(matches(not(isEnabled())))
    }
}