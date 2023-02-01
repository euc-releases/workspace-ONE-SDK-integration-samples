// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.ui

import android.os.Bundle
import com.airwatch.login.SDKBaseActivity
import com.example.integrationguide.R
import com.example.integrationguide.SDKProfilePresentation

class DLPWatermarkActivity : SDKBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlp_watermark)

        SDKProfilePresentation.displayWatermarkSettings(
            findViewById(R.id.policyWatermark),
            findViewById(R.id.policyWatermarkText),
            findViewById(R.id.view_inserted_watermark)
        )

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_dlp_watermark_views)
        }
    }

    //Set watermark on this activity which extends SDKBaseActivity
    override fun showWatermark(): Boolean = true

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