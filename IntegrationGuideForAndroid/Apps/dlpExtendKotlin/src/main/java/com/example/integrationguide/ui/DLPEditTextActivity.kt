// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.ui

import android.os.Bundle
import com.example.integrationguide.BaseActivity
import com.example.integrationguide.R
import com.example.integrationguide.SDKProfilePresentation

class DLPEditTextActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlp_edittext)

        SDKProfilePresentation.displayCopyPasteSettings(
            findViewById(R.id.policyOutbound),
            findViewById(R.id.policyInbound)
        )

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_dlp_edittext_views)
        }
    }

}
