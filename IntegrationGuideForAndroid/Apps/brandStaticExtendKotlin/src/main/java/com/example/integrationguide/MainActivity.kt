// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.airwatch.sdk.SDKManager
import com.airwatch.sdk.context.SDKContext
import com.airwatch.sdk.context.SDKContextManager

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureTextView()
        toastHere(configureStatus())
    }

    private fun configureStatus():String {
        val sdkContext = SDKContextManager.getSDKContext()
        val sdkManager = SDKManager.init(applicationContext)
        val status = getString(
            R.string.sdk_state, sdkContext.currentState.name)

        findViewById<TextView>(R.id.textViewConfiguration).let {
            it.text = status
            it.setOnClickListener { statusToggle() }
        }

        val message = listOf(
            if (sdkContext.currentState == SDKContext.State.IDLE)
                getString(R.string.sdk_configuration_unavailable)
            else
                sdkContext.sdkConfiguration.toString(),
            mapOf(
                "c" to sdkContext.currentState.value,
                "n" to sdkContext.currentState.name,
                "I" to SDKContext.State.INITIALIZED.value,
                "C" to SDKContext.State.CONFIGURED.value
            ).toString()
        ).joinToString(separator = "\n")

        // Dump the message into the textView that is inside the scrolling
        // portion.
        findViewById<TextView>(R.id.textViewScrolling).text = message

        return status
    }

    fun statusToggle() {
        val visible = findViewById<View>(R.id.scrollView).let { scrollView ->
            (scrollView.visibility == View.VISIBLE).also {
                scrollView.visibility = if (it) View.GONE else View.VISIBLE
            }
        }
        findViewById<View>(R.id.toggleView).visibility =
            if (visible) View.VISIBLE else View.GONE
    }

    private fun toastHere(message: String) { runOnUiThread {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show() }}
}
