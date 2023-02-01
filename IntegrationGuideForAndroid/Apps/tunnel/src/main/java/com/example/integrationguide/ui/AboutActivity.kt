// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.ui

import android.os.Bundle
import android.widget.EditText
import com.example.integrationguide.BaseActivity
import com.example.integrationguide.camelSpaced
import com.example.integrationguide.R

class AboutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_about)
        }

        findViewById<EditText>(R.id.moduleName).setText(
            resources.getString(R.string.MODULE_NAME).camelSpaced())
    }

    override fun onResume() {
        super.onResume()

        // onResume is the callback invoked when re-opening the app after the
        // dark mode setting has been changed in the system settings.
        findViewById<EditText>(R.id.darkMode).setText(
            if (darkMode) R.string.dark_mode_true else R.string.dark_mode_false
        )
    }
}