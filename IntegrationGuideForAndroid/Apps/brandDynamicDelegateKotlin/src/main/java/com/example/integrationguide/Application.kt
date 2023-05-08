// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.airwatch.app.AWSDKApplication
import com.airwatch.app.AWSDKApplicationDelegate
import com.airwatch.event.WS1AnchorEvents
import java.security.cert.X509Certificate

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

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        attachBaseContext(this)
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

    override fun onSSLPinningValidationFailure(host: String, serverCACert: X509Certificate?) {
    }

    override fun onSSLPinningRequestFailure(p0: String, p1: X509Certificate?) {
    }

    override fun getEventHandler(): WS1AnchorEvents {
        return AppWS1AnchorEvents()
    }
}