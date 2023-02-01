// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*

/*
This test uses both UIAutomator and Espresso.

-   UIAutomator is used in the @Before to launch the app and wait for the
    MainActivity to appear.
-   Espresso is used in the @Test code.

The test can't use Espresso ActivityScenarioRule<MainActivity> because the
MainActivity can't be launched. The Workspace ONE SplashActivity will be
launched first.
*/

private const val LAUNCH_TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)
class BaseActivityTest {
    private val packageName:String = this.javaClass.`package`!!.name

    // TOTH this function started with a copy of code from:
    // https://developer.android.com/training/testing/ui-testing/uiautomator-testing#accessing-ui-components
    @Before
    fun startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        val device = UiDevice.getInstance(
            InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT
        )

        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
            packageName)!!.apply {
            // Clear out any previous instances
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        // Wait for the MainActivity to appear, by waiting for a particular
        // View, specified by its resource name.
        val resources = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources
        device.wait(
            Until.hasObject(By.res(resources.getResourceName(
                R.id.uiPlaceholder))),
            LAUNCH_TIMEOUT * 10
            // Increased launch time out to allow time for a human to set a
            // passcode or whatever.
        )
    }

    @Test
    fun moduleName() {
        // TOTH getting the app context:
        // https://stackoverflow.com/a/30890792/7657675
        val resources = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources

        onView(withId(R.id.uiPlaceholder))
            .check(matches(withText(R.string.ui_placeholder)))

        // Open the About screen and check that the module name is shown.
        //
        // TOTH how to interact with the options menu in Espresso
        // https://stackoverflow.com/a/33834533/7657675
        onView(withId(R.id.about)).perform(click())
        onView(withId(R.id.moduleName)).check(matches(withText(
            resources.getString(R.string.MODULE_NAME).camelSpaced()
        )))
        // To force failure, comment out the .camelSpaced() call.
    }
}
