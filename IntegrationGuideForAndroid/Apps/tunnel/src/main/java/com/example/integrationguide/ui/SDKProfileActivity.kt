// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.airwatch.core.task.TaskResult
import com.airwatch.sdk.configuration.BaseConfiguration
import com.airwatch.sdk.configuration.SDKFetchSettingsHelper
import com.airwatch.sdk.context.ISdkFetchSettingsListener
import com.airwatch.sdk.context.SDKContext
import com.airwatch.sdk.context.SDKContextManager
import com.example.integrationguide.BaseActivity
import com.example.integrationguide.R

class SDKProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdk_profile)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_sdk_profile)
        }

        configureStatus()
        lastFetchError = null
        findViewById<View>(R.id.fetchButton)?.setOnClickListener() {
            fetchSettings()
        }
        indicateNotFetching(null)
    }

    private fun configureStatus(message: String): String {
        val sdkContext = SDKContextManager.getSDKContext()
        // val sdkManager = SDKManager.init(applicationContext)
        val status = getString(
            R.string.sdk_state, sdkContext.currentState.name)
        findViewById<TextView>(R.id.textViewStatus).text = status

        // Dump the message into the textView inside the scrolling portion.
        findViewById<TextView>(R.id.scrollingTextView).text = message
        return status
    }

    private fun configureStatus(): String {
        val sdkContext = SDKContextManager.getSDKContext()
        return configureStatus(listOf(
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
        ).joinToString(separator = "\n"))
    }

    private fun fetchSettings() {
        val originalAlpha = indicateFetching()

        SDKFetchSettingsHelper(this).fetchSDKSettings(this,
            object : ISdkFetchSettingsListener {
                override fun onSuccess(configuration: BaseConfiguration?) {
                    indicateNotFetching(originalAlpha)
                    lastFetchError = null
                    configureStatus()
                }

                override fun onFailure(result: TaskResult?) {
                    indicateNotFetching(originalAlpha)
                    lastFetchError = if (result == null) "onFailure(null)"
                    else
                        result.payload?.toString() ?: "onFailure(payload null)"
                }

            }
        )
    }

    var lastFetchError: String? = null
        set(value) {
            field = value
            findViewById<TextView>(R.id.lastFetchError)?.run {
                text = value ?: ""
                visibility =
                    if (value?.isBlank() != false) View.GONE else View.VISIBLE
            }
        }

    private fun indicateFetching(): Float? {
        findViewById<View>(R.id.fetchButton)?.isEnabled = false
        findViewById<View>(R.id.fetchingIndicator)?.visibility = View.VISIBLE
        findViewById<TextView>(R.id.scrollingTextView)?.let { view ->
            return view.alpha.also { view.alpha = view.alpha.div(3F) }
        }
        return null
    }

    private fun indicateNotFetching(originalAlpha: Float?) {
        findViewById<View>(R.id.fetchButton)?.isEnabled = true
        findViewById<View>(R.id.fetchingIndicator)?.visibility = View.GONE
        findViewById<TextView>(R.id.scrollingTextView)?.let { view ->
            originalAlpha?.let { view.alpha = originalAlpha }
        }
    }

}
