// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.widget.TextView

// This class is for subclass creation only.
@SuppressLint("Registered")
open class BaseActivity : Activity() {

    private val uiTexts by lazy {generateUITexts()}

    open fun generateUITexts():List<CharSequence> {
        return listOf(
            resources.getString(R.string.ui_placeholder),
            resources.getString(R.string.MODULE_NAME).camelSpaced(),
            with(resources.configuration.uiMode) {
                if ((this and Configuration.UI_MODE_NIGHT_MASK)
                    == Configuration.UI_MODE_NIGHT_YES
                ) "Dark mode" else "Not dark mode"
            }
        )
    }

    protected fun configureTextView(textViewID: Int = R.id.textViewIntegration) {
        var uiTextIndex = 0
        with(findViewById<TextView>(textViewID)) {
            text = uiTexts[0]
            setOnClickListener {
                uiTextIndex = (uiTextIndex + 1) % uiTexts.count()
                text = uiTexts[uiTextIndex]
            }
        }
    }

}
