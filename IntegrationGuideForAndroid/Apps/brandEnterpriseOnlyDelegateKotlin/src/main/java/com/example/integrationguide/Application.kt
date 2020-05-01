// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.airwatch.app.AWSDKApplication
import com.airwatch.app.AWSDKApplicationDelegate

// This class uses Kotlin delegation to implement the AWSDKApplication
// interface.  
// A new AWSDKApplicationDelegate instance is allocated on the fly as the
// delegate. For background on Kotlin delegation, see:
// https://kotlinlang.org/docs/reference/delegation.html

open class Application:
    android.app.Application(),
    AWSDKApplication by AWSDKApplicationDelegate()
{
    // Android Application overrides for integration.
    override fun onCreate() {
        super.onCreate()
        onCreate(this)
    }

    override fun getSystemService(name: String): Any? {
        return getAWSystemService(name, super.getSystemService(name))
    }

    // Application-specific overrides.
    override fun onPostCreate() {
        // Code from the application's original onCreate() would go here.
    }

    override fun getMainActivityIntent(): Intent {
        // Replace MainActivity with application's original main activity.
        return Intent(applicationContext, MainActivity::class.java)
    }

    override fun getNightMode(): Int {
        return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}