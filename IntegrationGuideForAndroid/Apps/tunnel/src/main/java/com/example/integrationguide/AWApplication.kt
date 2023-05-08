// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.airwatch.event.WS1AnchorEvents
import java.security.cert.X509Certificate

// Note the fully qualified base class name.
open class AWApplication: com.airwatch.app.AWApplication() {
    override fun getMainActivityIntent(): Intent {
        return Intent(applicationContext, MainActivity::class.java)
    }

    override fun onSSLPinningRequestFailure(
        host: String,
        serverCACert: X509Certificate?
    ) {
    }

    override fun onSSLPinningValidationFailure(
        host: String,
        serverCACert: X509Certificate?
    ) {
    }

    override fun getNightMode(): Int {
        return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    override fun getEventHandler(): WS1AnchorEvents {
        return AppWS1AnchorEvents()
    }
}
