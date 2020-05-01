// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.airwatch.sdk.SDKManager
import com.airwatch.sdk.context.SDKContextManager

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureTextView()

        val sdkContext = SDKContextManager.getSDKContext()
        val sdkManager = SDKManager.init(applicationContext)
        val configuration = sdkManager.applicationConfiguration
        toastHere("SDK state:${sdkContext.currentState.name}.")
    }

    private fun toastHere(message: String) { runOnUiThread {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show() }}
}
