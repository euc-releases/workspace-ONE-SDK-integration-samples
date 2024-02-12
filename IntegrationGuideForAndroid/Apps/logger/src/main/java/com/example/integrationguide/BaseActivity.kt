// Copyright 2024 VMware LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.annotation.SuppressLint
import android.content.res.Configuration
import com.airwatch.login.SDKBaseActivity

// This class is for subclass creation only.
@SuppressLint("Registered")
open class BaseActivity : SDKBaseActivity() {
    // Utility property to give a simple Boolean for the current dark-mode
    // selection.
    val darkMode
        get() = resources.configuration.uiMode.run {
            ((this and Configuration.UI_MODE_NIGHT_MASK)
                    == Configuration.UI_MODE_NIGHT_YES)
        }

    // TOTH for NavigateUp callback functions:
    // https://stackoverflow.com/a/56234724/7657675
    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
