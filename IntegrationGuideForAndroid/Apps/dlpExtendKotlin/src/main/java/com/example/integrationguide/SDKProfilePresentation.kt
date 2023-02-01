// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.content.Context
import android.widget.EditText
import com.airwatch.login.ui.AWWatermarkView
import com.airwatch.sdk.configuration.SDKConfigurationKeys
import com.airwatch.sdk.context.SDKContext
import com.airwatch.sdk.context.SDKContextManager


object SDKProfilePresentation {

    fun sdkState(context: Context): String = context.getString(
        R.string.sdk_state, SDKContextManager.getSDKContext().currentState.name
    )

    fun displayCopyPasteSettings(
        outboundEditText: EditText?, inboundEditText: EditText?
    ) {
        val copyPastePolicyOutbound: Boolean?
        val copyPastePolicyInbound: Boolean?

        val sdkContext = SDKContextManager.getSDKContext()
        if (sdkContext.currentState == SDKContext.State.IDLE) {
            copyPastePolicyInbound = null
            copyPastePolicyOutbound = null
        } else {
            // For now, these settings are logically inverted so that the
            // semantics are Active or Inactive.
            copyPastePolicyOutbound = sdkContext.sdkConfiguration
                ?.getSettings(SDKConfigurationKeys.TYPE_DATA_LOSS_PREVENTION)
                ?.getBoolean(SDKConfigurationKeys.ENABLE_COPY_PASTE)
                ?.not()
            copyPastePolicyInbound = sdkContext.sdkConfiguration
                ?.getSettings(SDKConfigurationKeys.TYPE_DATA_LOSS_PREVENTION)
                ?.getBoolean(SDKConfigurationKeys.ENABLE_COPY_PASTE_INTO)
                ?.not()
        }
        outboundEditText?.setText(
            policyDisplay(copyPastePolicyOutbound, outboundEditText.context)
        )
        inboundEditText?.setText(
            policyDisplay(copyPastePolicyInbound, inboundEditText.context)
        )
    }

    private fun policyDisplay(setting: Boolean?, context: Context): String {
        return when (setting) {
            true -> context.getString(R.string.policy_true_active)
            false -> context.getString(R.string.policy_false_inactive)
            else -> context.getString(R.string.policy_null)
        }
    }

    fun displayPasswordTimeOutSettings(timeOutEditText: EditText?): Long? {
        val timeOut: String?

        val sdkContext = SDKContextManager.getSDKContext()
        if (sdkContext.currentState == SDKContext.State.IDLE) {
            timeOut = null
        } else {
            // Get PASSCODE_TIMEOUT value from TYPE_PASSCODE_POLICY Bundle.
            timeOut = sdkContext.sdkConfiguration
                ?.getSettings(SDKConfigurationKeys.TYPE_PASSCODE_POLICY)
                ?.getString(SDKConfigurationKeys.PASSCODE_TIMEOUT)
        }
        timeOutEditText?.setText(
            timeOut ?: timeOutEditText.context.getString(R.string.policy_null)
        )

        return timeOut?.toLong()
    }

    fun displayWatermarkSettings(
        watermarkPolicyEditText: EditText,
        watermarkText: EditText,
        watermarkView: AWWatermarkView
    ) {
        //Grade SDKContextManager to get DLP Settings configuration
        val dlpSettings = SDKContextManager.getSDKContext().sdkConfiguration
            .getSettings(SDKConfigurationKeys.TYPE_DATA_LOSS_PREVENTION)
        //Query settings configuration to grab the Watermark enablement
        //and watermark text
        val enableWatermark = dlpSettings
            .getString(SDKConfigurationKeys.ENABLE_WATERMARK, "false")
        val text = dlpSettings.getString(SDKConfigurationKeys.OVERLAY_TEXT, "")

        //Set policy indicator and text on the demo page
        watermarkPolicyEditText.setText(
            policyDisplay(
                enableWatermark.lowercase() == "true",
                watermarkPolicyEditText.context
            )
        )
        watermarkText.setText(text)

        //This line is an example of how you programmatically set the text on a View Inserted Watermark.
        //Here just setting it to a default which will differ from a watermark set in the console.
        watermarkView.text = watermarkView.context.getString(R.string.default_view_watermark)
    }
}