// Copyright 2020 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.airwatch.sdk.SDKManager
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : BaseActivity() {

    private var sdkManager: SDKManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureTextView()
        configureStatus()

        startSDK()
    }

    private fun configureStatus() {
        findViewById<TextView>(R.id.textViewConfiguration).let {
            it.text = getString(R.string.status_placeholder)
            it.setOnClickListener { statusToggle() }
        }
    }

    private fun statusToggle() {
        val visible = findViewById<View>(R.id.scrollView).let { scrollView ->
            !(scrollView.visibility == View.VISIBLE).also {
                scrollView.visibility = if (it) View.GONE else View.VISIBLE
            }
        }
        findViewById<View>(R.id.toggleView).visibility =
            if (visible) View.VISIBLE else View.GONE
    }

    private fun startSDK() { thread {
        try {
            val initSDKManager = SDKManager.init(this)
            sdkManager = initSDKManager
            val message = getString(
                R.string.status_ok, initSDKManager.consoleVersion.toString())
            toastHere(message)
            showStatus(
                message,
                JSONObject(initSDKManager.sdkProfileJSONString).toString(4)
            )
        }
        catch (exception: Exception) {
            sdkManager = null
            val message = getString(R.string.status_ng)
            toastHere(message)
            showStatus(message, exception.toString())
        }
    }}

    private fun showStatus(shortMessage: String, longMessage: String) {
        runOnUiThread {
            findViewById<TextView>(R.id.textViewConfiguration).text =
                shortMessage
            findViewById<TextView>(R.id.textViewScrolling).text =
                longMessage
        }
    }

    private fun toastHere(message: String) { runOnUiThread {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show() }}
}
