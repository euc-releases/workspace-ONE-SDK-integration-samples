// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.airwatch.sdk.SDKManager
import kotlin.concurrent.thread

class MainActivity : BaseActivity() {

    private var sdkManager: SDKManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureTextView()

        startSDK()
    }

    private fun startSDK() { thread {
        try {
            val initSDKManager = SDKManager.init(this)
            sdkManager = initSDKManager
            toastHere("Workspace ONE console version:${initSDKManager.consoleVersion}")

        }
        catch (exception: Exception) {
            sdkManager = null
            toastHere("Workspace ONE failed $exception.") }
    }}

    private fun toastHere(message: String) { runOnUiThread {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show() }}
}
